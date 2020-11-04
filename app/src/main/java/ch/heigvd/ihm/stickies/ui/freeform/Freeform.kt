package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
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
import ch.heigvd.ihm.stickies.ui.StickiesFakeWhite
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.GridHorizontalCellCount
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.GridVerticalCellCount
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.PileAngles
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.PileOffsetX
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.PileOffsetY
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.StickyMaxStiffness
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.StickyMinStiffness
import ch.heigvd.ihm.stickies.ui.modifier.offset
import ch.heigvd.ihm.stickies.ui.modifier.offsetPx
import ch.heigvd.ihm.stickies.ui.stickies.*
import ch.heigvd.ihm.stickies.util.fastForEachIndexedReversed

/**
 * Calculates the offset to be applied to a cell at a certain grid index to hide it from the
 * category detail view.
 *
 * @param index the grid index of the item to hide.
 * @param origin the origin of the plane in which items are dropped.
 * @param cellSize the dimensions of a cell that is displayed.
 * @param size the size of the plane that the items are displayed on.
 *
 * @return the [Offset] at which the items will be placed when hidden.
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun hiddenOffset(
    index: Int,
    origin: Offset,
    cellSize: Offset,
    spacer: Offset,
    size: Offset,
): Offset {
    return restOffset(index, origin, cellSize, spacer)
        .plus(Offset(x = 0f, y = -size.y))
}

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
    WithConstraints(
        modifier
            .background(Color.StickiesFakeWhite)
            .fillMaxSize(),
    ) {
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

        // Render all the category placeholders.
        model.categories.fastForEachIndexed { index, category ->
            key(category) {
                val color = animate(
                    if (model.open != null) Color.StickiesFakeWhite
                    else contentColorFor(color = MaterialTheme.colors.surface)
                )
                Placeholder(
                    title = category.title,
                    asset = vectorResource(id = category.icon),
                    color = color,
                    modifier = Modifier.offset(
                        restOffset(
                            index = index,
                            origin = start,
                            cellSize = stickySizeOffset,
                            spacer = spacer,
                        )
                    )
                )
            }
        }

        // Render, for each category, all of the involved stickies.
        model.categories.fastForEachIndexed { catIndex, category ->
            category.stickies.fastForEachIndexedReversed { stiIndex, sticky ->
                val stickyRestOffset = when {
                    model.open == catIndex -> Offset.Zero // TODO : Change this.
                    model.isOpen -> hiddenOffset(catIndex, start, stickySizeOffset, spacer, size)
                    else -> restOffset(catIndex, start, stickySizeOffset, spacer)
                }
                val dragOffset = when {
                    model.open == catIndex -> Offset.Zero
                    model.isOpen ->
                        if (sticky.dragState is DragState.DraggingSingle) sticky.dragState.dragOffset
                        else Offset.Zero
                    else -> category.stickies.pileOffset()
                }
                val dragState = sticky.dragState

                val isSelfOpen = model.open == catIndex
                val isAnyOpen = model.isOpen

                key(sticky.sticky.identifier) {
                    FreeformSticky(
                        bubbled = sticky.sticky.highlighted,
                        dragged = sticky.dragState is DragState.DraggingSingle ||
                                category.stickies.any { it.dragState is DragState.DraggingPile },
                        offset = stickyRestOffset + dragOffset,
                        title = sticky.sticky.title,
                        color = sticky.sticky.color,
                        pileIndex = stiIndex,
                        pileSize = category.stickies.size,
                        onClick = {
                            if (isSelfOpen) {
                                setModel(model.copy(open = null))
                            } else {
                                setModel(model.copy(open = catIndex))
                            }
                        },
                        onDragStarted = {
                            when {
                                isSelfOpen -> {
                                    setModel(
                                        model.updateDrag(
                                            categoryIndex = catIndex,
                                            stickyIndex = stiIndex,
                                            dragState = DragState.DraggingSingle(),
                                        )
                                    )
                                }
                                isAnyOpen -> { /* Ignored. */
                                }
                                else -> {
                                    setModel(
                                        model.updateDrag(
                                            categoryIndex = catIndex,
                                            stickyIndex = stiIndex,
                                            dragState = DragState.DraggingPile(),
                                        )
                                    )
                                }
                            }
                        },
                        onDragStopped = {
                            when {
                                isSelfOpen && dragState is DragState.DraggingSingle -> {
                                    setModel(
                                        model.updateDrag(
                                            categoryIndex = catIndex,
                                            stickyIndex = stiIndex,
                                            dragState = DragState.NotDragging,
                                        )
                                    )
                                }
                                !isAnyOpen && dragState is DragState.DraggingPile -> {
                                    val offset = stickyRestOffset + dragState.dragOffset
                                    val dismissedDrag = model.updateDrag(
                                            categoryIndex = catIndex,
                                            stickyIndex = stiIndex,
                                            dragState = DragState.NotDragging,
                                        )
                                    setModel(
                                        dismissedDrag.swapCategories(
                                            dropIndex(offset, start, size),
                                            catIndex
                                        )
                                    )
                                }
                                else -> {
                                    setModel(
                                        model.updateDrag(
                                            categoryIndex = catIndex,
                                            stickyIndex = stiIndex,
                                            dragState = DragState.NotDragging,
                                        )
                                    )
                                }
                            }
                        },
                        onDragOffset = { offset ->
                            when {
                                isSelfOpen -> {
                                    when (dragState) {
                                        is DragState.DraggingPile -> {
                                            setModel(
                                                model.updateDrag(
                                                    categoryIndex = catIndex,
                                                    stickyIndex = stiIndex,
                                                    dragState = DragState.NotDragging,
                                                )
                                            )
                                        }
                                        is DragState.DraggingSingle -> {
                                            setModel(
                                                model.updateDrag(
                                                    categoryIndex = catIndex,
                                                    stickyIndex = stiIndex,
                                                    dragState = DragState.DraggingSingle(
                                                        dragState.dragOffset + offset
                                                    ),
                                                )
                                            )
                                        }
                                        is DragState.NotDragging -> {
                                            /* Ignored. */
                                        }
                                    }
                                }
                                isAnyOpen -> {
                                    setModel(
                                        model.updateDrag(
                                            categoryIndex = catIndex,
                                            stickyIndex = stiIndex,
                                            dragState = DragState.NotDragging,
                                        )
                                    )
                                }
                                else -> {
                                    when (dragState) {
                                        is DragState.DraggingPile -> {
                                            setModel(
                                                model.updateDrag(
                                                    categoryIndex = catIndex,
                                                    stickyIndex = stiIndex,
                                                    dragState = DragState.DraggingPile(
                                                        dragState.dragOffset + offset
                                                    ),
                                                )
                                            )
                                        }
                                        is DragState.DraggingSingle -> {
                                            setModel(
                                                model.updateDrag(
                                                    categoryIndex = catIndex,
                                                    stickyIndex = stiIndex,
                                                    dragState = DragState.NotDragging,
                                                )
                                            )
                                        }
                                        is DragState.NotDragging -> {
                                            /* Ignored. */
                                        }
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

// FREEFORM COMPOSABLES.

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
 * @param onClick called when this sticky is clicked.
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
    onClick: () -> Unit,
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
                // TODO : Make this an attribute of Sticky.
                .clickable(onClick = onClick, indication = null)
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

object FreeformConstants {

    // Stiffness that's given to the different springs.
    const val StickyMinStiffness = Spring.StiffnessVeryLow
    const val StickyMaxStiffness = Spring.StiffnessLow

    // Angles and offsets applied to stickies, depending on their pile index.
    val PileAngles = listOf(0f, 3f, 2f)
    val PileOffsetX = listOf(0.dp, 4.dp, (-4).dp)
    val PileOffsetY = listOf(0.dp, (-6).dp, (-6).dp)

    // Grid dimensions.
    const val GridHorizontalCellCount = 3
    const val GridVerticalCellCount = 2
}