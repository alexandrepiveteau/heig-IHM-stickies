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
import androidx.compose.ui.Modifier
import androidx.compose.ui.TransformOrigin
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.LongPressDragObserver
import androidx.compose.ui.gesture.longPressDragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.HapticFeedBackAmbient
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.zIndex
import ch.heigvd.ihm.stickies.ui.StickiesFakeWhite
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.PileAngles
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.PileOffsetX
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.PileOffsetY
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.StickyMaxStiffness
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.StickyMinStiffness
import ch.heigvd.ihm.stickies.ui.modifier.offset
import ch.heigvd.ihm.stickies.ui.modifier.offsetPx
import ch.heigvd.ihm.stickies.ui.stickies.*
import ch.heigvd.ihm.stickies.util.fastForEachIndexedReversed

/**
 * Calculates the offset to be applied to a cell at a certain grid index to hide it from the
 * category detail view.
 *
 * @param index the grid index of the item to hide.
 *
 * @return the [Offset] at which the items will be placed when hidden.
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun FreeformScope.hiddenOffset(
    index: Int,
): Offset {
    return restOffset(index)
        .plus(Offset(x = 0f, y = -size.y))
}

/**
 * Calculates the offset at which an item at a certain index should be positioned to be at rest.
 *
 * @param index the index for which we want the rest offset.
 *
 * @return the [Offset] at which the item should be put at rest.
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun FreeformScope.restOffset(
    index: Int,
): Offset {
    val horizontalCount = index % FreeformConstants.GridHorizontalCellCount
    val verticalCount = (index - horizontalCount) / FreeformConstants.GridVerticalCellCount
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
 *
 * @return the index of the drop position.
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun FreeformScope.dropIndex(
    position: Offset,
): Int {
    // TODO : This implementation completely ignores the spacings. Maybe this is something we'll
    //        actually want to take into account.
    val delta = position - origin
    val horizontal = size.x / FreeformConstants.GridHorizontalCellCount
    val vertical = size.y / FreeformConstants.GridVerticalCellCount

    val horizontalCount = (delta.x / horizontal).toInt()
    val verticalCount = (delta.y / vertical).toInt()

    return FreeformConstants.GridHorizontalCellCount * verticalCount + horizontalCount
}

@Composable
fun Pane(modifier: Modifier = Modifier) {
    Freeform(
        modifier
            .background(Color.StickiesFakeWhite)
            .fillMaxSize()
    ) {

        // Set the initial model.
        val (model, setModel) = remember { mutableStateOf(initialModel) }

        // Render all the category placeholders.
        // TODO : Add support for draggable categories.
        model.categories.fastForEachIndexed { index, category ->
            key(category) {
                val color = if (model.open != null) Color.StickiesFakeWhite
                else contentColorFor(color = MaterialTheme.colors.surface)

                Placeholder(
                    title = category.title,
                    asset = vectorResource(id = category.icon),
                    color = animate(color),
                    modifier = Modifier
                        .offset(animate(restOffset(index)))
                        .clickable(onClick = {
                            if (!model.isOpen) {
                                setModel(model.swapCategories(index, 0))
                            }
                        }, indication = null)

                )
            }
        }

        val piles = model.stickies.groupBy { it.categoryIndex }.toList()

        // Render all the sticky piles.
        for ((_, stickies) in piles) {

            // Render, for each category, all of the involved stickies.
            stickies.fastForEachIndexedReversed { stiIndex, sticky ->
                key(sticky.sticky.identifier) {

                    // TODO : Investigate if there is a nicer way to fix that.
                    //
                    // Reset the drag offset each time we're actually starting or stopping drag
                    // events. This is an ugly way to make sure that drag state is preserved and
                    // memoized locally, and that gestures across multiple stickies are actually
                    // possible.
                    val (dragOffset, setDragOffset) = remember(sticky.dragState.position != null) {
                        mutableStateOf(sticky.dragState.position)
                    }

                    val catIndex = sticky.categoryIndex
                    val stickyRestOffset = when {
                        // TODO : Change this.
                        model.open == sticky.categoryIndex -> origin + Offset(
                            (size.x - cellSize.x) / 2,
                            (size.y - cellSize.y) / 2,
                        )
                        model.isOpen -> hiddenOffset(catIndex)
                        else -> restOffset(catIndex)
                    }
                    val position = when {
                        dragOffset != null -> dragOffset
                        else -> stickyRestOffset
                    }

                    val isSelfOpen = model.open == catIndex
                    val isAnyOpen = model.isOpen

                    FreeformSticky(
                        bubbled = sticky.sticky.highlighted,
                        dragged = dragOffset != null,
                        offset = position,
                        title = sticky.sticky.title,
                        color = sticky.sticky.color,
                        pileIndex = stiIndex,
                        pileSize = stickies.size,
                        onClick = {
                            if (isSelfOpen) {
                                setModel(model.copy(open = null))
                            } else {
                                setModel(model.copy(open = catIndex))
                            }
                        },
                        onDragStarted = {
                            if (isSelfOpen || !isAnyOpen) {
                                setModel(
                                    model.updateStickyDrag(
                                        sticky.sticky.identifier,
                                        Dragging(position)
                                    )
                                )
                                setDragOffset(position)
                            }
                        },
                        onDragStopped = {
                            when {
                                !isAnyOpen -> {
                                    setModel(
                                        model
                                            .move(sticky.sticky.identifier, dropIndex(position))
                                            .updateStickyDrag(
                                                sticky.sticky.identifier,
                                                NotDragging()
                                            )
                                    )
                                    // TODO : Maybe we can get rid of that somehow.
                                    setDragOffset(null)
                                }
                                else -> {
                                    setModel(
                                        model.updateStickyDrag(
                                            sticky.sticky.identifier,
                                            NotDragging()
                                        )
                                    )
                                    // TODO : Maybe we can get rid of that somehow.
                                    setDragOffset(null)
                                }
                            }
                        },
                        onDragOffset = { offset ->
                            if (sticky.dragState.isDragging) {
                                // TODO : Maybe we can get rid of that somehow.
                                setDragOffset(offset + position)
                                setModel(
                                    // Supposition : we do not always get the latest position.
                                    model.updateStickyDrag(
                                        sticky.sticky.identifier,
                                        Dragging(position + offset)
                                    )
                                )
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
    val base = elevation.value + ((pileSize - (pileIndex + 1)) / pileSize.toFloat()) + 1
    val supplement = if (dragged) 1.0f else 0f

    Bubble(
        visible = bubbled,
        modifier
            .offsetPx(animate(offset.x, spring), animate(offset.y, spring))
            .size(StickySize)
            .drawLayer(scaleX = scale, scaleY = scale)
            .zIndex(base + supplement)
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
                    rotationZ = animate(PileAngles[pileIndex % PileAngles.size]),
                    transformOrigin = TransformOrigin.Center,
                ).offset(
                    x = animate(PileOffsetX[pileIndex % PileOffsetX.size]),
                    y = animate(PileOffsetY[pileIndex % PileOffsetY.size]),
                )
        )
    }
}

object PaneConstants {

    // Stiffness that's given to the different springs.
    const val StickyMinStiffness = Spring.StiffnessVeryLow
    const val StickyMaxStiffness = Spring.StiffnessLow

    // Angles and offsets applied to stickies, depending on their pile index.
    val PileAngles = listOf(0f, 3f, 2f)
    val PileOffsetX = listOf(0.dp, 4.dp, (-4).dp)
    val PileOffsetY = listOf(0.dp, (-6).dp, (-6).dp)
}