package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.LongPressDragObserver
import androidx.compose.ui.gesture.longPressDragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.HapticFeedBackAmbient
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import ch.heigvd.ihm.stickies.model.Sticky
import ch.heigvd.ihm.stickies.ui.modifier.offset
import ch.heigvd.ihm.stickies.ui.modifier.offsetPx
import ch.heigvd.ihm.stickies.ui.stickies.*
import ch.heigvd.ihm.stickies.util.fastForEachIndexedReversed

/**
 * Calculates the offset at which an item at a certain index should be positioned to be at rest.
 *
 * @param index the index for which we want the rest offset.
 * @param origin the origin of the plane in which items are dropped.
 * @param cellSize the dimensions of a cell that is displayed.
 * @param spacer the spacer applied between items.
 *
 * @return the [Offset] at which the item should be put at rest.
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun restOffset(
    index: Int,
    origin: Offset,
    cellSize: Offset,
    spacer: Offset,
): Offset {
    val horizontalCount = index % GridHorizontalCellCount
    val verticalCount = (index - horizontalCount) / GridVerticalCellCount
    // Items have at least one "unit" of spacer at the top left.
    return origin + Offset(
        spacer.x + (horizontalCount * (spacer.x + cellSize.x)),
        spacer.y + (verticalCount * (spacer.y + cellSize.y)),
    )
}

/**
 * Calculates the drop index of a document, depending on the grid dimensions.
 *
 * @param position the [Offset] at which the item is dropped.
 * @param origin the origin of the plane in which items are dropped.
 * @param size the size of the pane in which the items are dropped.
 *
 * @return the index of the drop position.
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun dropIndex(
    position: Offset,
    origin: Offset,
    size: Offset,
): Int {
    // TODO : This implementation completely ignores the spacings. Maybe this is something we'll
    //        actually want to take into account.
    val delta = position - origin
    val horizontal = size.x / GridHorizontalCellCount
    val vertical = size.y / GridVerticalCellCount

    val horizontalCount = (delta.x / horizontal).toInt()
    val verticalCount = (delta.y / vertical).toInt()

    return GridHorizontalCellCount * verticalCount + horizontalCount
}

@Composable
fun Freeform(
    modifier: Modifier = Modifier,
) {
    WithConstraints(modifier.fillMaxSize()) {
        val width = with(DensityAmbient.current) { maxWidth.toPx() }
        val height = with(DensityAmbient.current) { maxHeight.toPx() }
        val size = Offset(x = width, y = height)
        val stickySize = with(DensityAmbient.current) { StickySize.toPx() }
        val stickySizeOffset = Offset(stickySize, stickySize)

        // Paddings to be respected between the grid elements.
        val spacerX = (width - (GridHorizontalCellCount * stickySize)) /
                (GridHorizontalCellCount + 1)
        val spacerY = (height - (GridVerticalCellCount * stickySize)) /
                (GridVerticalCellCount + 1)
        val spacer = Offset(x = spacerX, y = spacerY)

        // Initial offset to apply to the elements to start at the top start corner.
        val startX = (stickySize - width) / 2
        val startY = (stickySize - height) / 2
        val start = Offset(x = startX, y = startY)

        // Set the initial model.
        val (model, setModel) = remember { mutableStateOf(initialModel) }

        model.categories.fastForEachIndexed { index, category ->
            val rest = restOffset(
                index,
                origin = start,
                cellSize = stickySizeOffset,
                spacer = spacer,
            )
            val (offset, setOffset) = remember { mutableStateOf(rest) }

            // TODO : Check if there's a way to do without key() if we're animating across screens.
            key(category) {
                Placeholder(
                    title = category.title,
                    asset = vectorResource(id = category.icon),
                    Modifier.offset(
                        rest + Offset(
                            x = (width - stickySize) / 2,
                            y = (height - stickySize) / 2
                        )
                    )
                )
            }
            key(category.stickies) {
                FreeformPile(
                    stickies = category.stickies,
                    restOffset = rest,
                    onDrag = setOffset,
                    onDrop = {
                        val droppedAt = dropIndex(offset, start, size)

                        // Cascade our model update.
                        setModel(model.swap(droppedAt, index))
                    },
                )
            }
        }
    }
}

@Composable
private fun WithConstraintsScope.Debug() {
    val width = with(DensityAmbient.current) { maxWidth.toPx() }
    val height = with(DensityAmbient.current) { maxHeight.toPx() }
    val size = with(DensityAmbient.current) { StickySize.toPx() }
    ProvideEmphasis(AmbientEmphasisLevels.current.disabled) {
        Column {
            Text("Size : $width x $height")
            Text("Sticky size : $size x $size")
        }
    }
}

// FREEFORM COMPOSABLES.

/**
 * A freeform pile of stickies, that is inlined at call site.
 *
 * @param stickies the [List] of all the stickies in the pile.
 * @param restOffset the offset at which the items are displayed when not dragged.
 * @param onDrag callback that returns the absolute offset of the pile when it is dragged.
 * @param onDrop callback that is called when the pile is dropped.
 */
@Suppress("NOTHING_TO_INLINE")
@Composable
private inline fun FreeformPile(
    stickies: List<Sticky>,
    restOffset: Offset,
    noinline onDrag: (absolute: Offset) -> Unit,
    noinline onDrop: () -> Unit,
) {
    val (dragged, setDragged) = remember { mutableStateOf(false) }
    val (dragOffset, setDragOffset) = remember { mutableStateOf(Offset.Zero) }

    val bubbled = remember(stickies) { stickies.any { it.highlighted } }

    stickies.fastForEachIndexedReversed { index, sticky ->
        key(sticky.identifier) {
            if (index == 0) {
                FreeformSticky(
                    bubbled = bubbled,
                    dragged = dragged,
                    offset = restOffset + dragOffset,
                    title = sticky.title,
                    color = sticky.color,
                    pileIndex = index,
                    pileSize = stickies.size,
                    onDragStarted = { setDragged(true) },
                    onDragOffset = { delta ->
                        setDragOffset(dragOffset + delta)
                        onDrag(restOffset + dragOffset + delta)
                    },
                    onDragStopped = {
                        setDragged(false)
                        setDragOffset(Offset.Zero)
                        onDrop()
                    },
                )
            } else {
                FreeformSticky(
                    bubbled = false,
                    dragged = dragged,
                    offset = restOffset + dragOffset,
                    title = sticky.title,
                    color = sticky.color,
                    pileIndex = index,
                    pileSize = stickies.size
                )
            }
        }
    }
}

/**
 * A composable that can display a floating sticky.
 *
 * @param bubbled true if a bubble should be displayed.
 * @param dragged true if this sticky is currently dragged.
 * @param offset what the offset for this sticky should be.
 * @param title the [String] content of the sticky.
 * @param color the [Color] used for the sticky surface.
 * @param pileIndex what's the index of this sticky in its pile
 * @param pileSize how many items are in this sticky pile.
 * @param onDragStarted called when a drag gesture starts on the sticky.
 * @param onDragOffset called when a drag gesture moves on the sticky.
 * @param onDragStopped called when a drag gesture stops on the sticky.
 * @param modifier the [Modifier] for this composable.
 */
@Composable
private fun FreeformSticky(
    bubbled: Boolean,
    dragged: Boolean,
    offset: Offset,
    title: String,
    color: Color,
    pileIndex: Int,
    pileSize: Int,
    modifier: Modifier = Modifier,
    onDragStarted: () -> Unit = {},
    onDragOffset: (Offset) -> Unit = {},
    onDragStopped: (speed: Offset) -> Unit = {},
) {
    val haptic = HapticFeedBackAmbient.current
    val stiffnessStep = if (pileSize > 1) {
        (StickyMaxStiffness - StickyMinStiffness) / (pileSize - 1)
    } else {
        0f
    }
    val spring = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = StickyMaxStiffness - (stiffnessStep * pileIndex),
        visibilityThreshold = 0.01f,
    )
    val dpSpring = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = StickyMaxStiffness,
        visibilityThreshold = 0.1.dp,
    )
    val scale = animate(if (dragged) 1.15f else 1.0f, spring)
    val elevation = animate(
        if (dragged) StickyRaisedElevation else StickyDefaultElevation,
        dpSpring,
    )

    Bubble(
        visible = bubbled,
        modifier
            .size(StickySize)
            .offsetPx(animate(offset.x, spring), animate(offset.y, spring))
            .drawLayer(scaleX = scale, scaleY = scale)
            .zIndex(elevation.value)
    ) {
        Sticky(
            text = title,
            color = color,
            elevation = elevation,
            modifier = Modifier
                .longPressDragGestureFilter(object : LongPressDragObserver {
                    override fun onLongPress(pxPosition: Offset) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDragStarted()
                    }

                    override fun onStop(velocity: Offset) {
                        onDragStopped(velocity)
                    }

                    override fun onDrag(dragDistance: Offset): Offset {
                        onDragOffset(dragDistance)
                        return super.onDrag(dragDistance)
                    }
                })
                .drawLayer(
                    rotationZ = PileAngles[pileIndex % PileAngles.size],
                    transformOrigin = TransformOrigin.Center,
                ).offset(
                    x = PileOffsetX[pileIndex % PileOffsetX.size],
                    y = PileOffsetY[pileIndex % PileOffsetY.size]
                )
        )
    }
}

// Stiffness that's given to the different springs.
private const val StickyMinStiffness = Spring.StiffnessVeryLow
private const val StickyMaxStiffness = Spring.StiffnessLow

// Stickies and layout metrics.
private val StickySize = 256.dp

// Angles and offsets applied to stickies, depending on their pile index.
private val PileAngles = listOf(0f, 3f, 2f)
private val PileOffsetX = listOf(0.dp, 4.dp, (-4).dp)
private val PileOffsetY = listOf(0.dp, (-6).dp, (-6).dp)

// Grid dimensions.
private const val GridHorizontalCellCount = 3
private const val GridVerticalCellCount = 2