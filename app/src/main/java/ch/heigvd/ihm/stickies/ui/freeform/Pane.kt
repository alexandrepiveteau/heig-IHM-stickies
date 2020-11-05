package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.TransformOrigin
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.LongPressDragObserver
import androidx.compose.ui.gesture.longPressDragGestureFilter
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.HapticFeedBackAmbient
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.zIndex
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.StickiesFakeWhite
import ch.heigvd.ihm.stickies.ui.StickiesNicerRed
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.GridHorizontalCellCount
import ch.heigvd.ihm.stickies.ui.freeform.FreeformConstants.GridVerticalCellCount
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.PileAngles
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.PileOffsetX
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.PileOffsetY
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.StickyMaxStiffness
import ch.heigvd.ihm.stickies.ui.freeform.PaneConstants.StickyMinStiffness
import ch.heigvd.ihm.stickies.ui.modifier.offset
import ch.heigvd.ihm.stickies.ui.modifier.offsetPx
import ch.heigvd.ihm.stickies.ui.stickies.*

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
    open: Int,
): Offset {
    return restOffset(index)
        .plus(
            Offset(
                x = 0f,
                y = if (open < (GridHorizontalCellCount * GridVerticalCellCount) / 2) size.y
                else -size.y
            )
        )
}

/**
 * Calculates the offset to be applied to a cell that is shown in the detail view.
 *
 * TODO : Provide some additional information related to scroll.
 *
 * @param index the grid index of the item to show.
 * @param scroll how much scroll amount there currently is.
 *
 * @return the [Offset] at which the item at the index-th position should be placed.
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun FreeformScope.detailOffset(
    index: Int,
    scroll: ScrollState,
): Offset {
    val horizontalCount = index % 2
    val verticalCount = (index - horizontalCount) / 2
    val topLeading = origin +
            Offset(x = 0f, y = -scroll.amount) +
            Offset(x = 2 * spacer.x, y = 0.5f * spacer.y) +
            Offset(x = cellSize.x, y = 0f)

    return topLeading + Offset(
        x = horizontalCount * (spacer.x + cellSize.x),
        y = verticalCount * (spacer.y + cellSize.y),
    )
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
    val horizontalCount = index % GridHorizontalCellCount
    val verticalCount = (index - horizontalCount) / GridHorizontalCellCount
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
    val horizontal = size.x / GridHorizontalCellCount
    val vertical = size.y / GridVerticalCellCount

    val horizontalCount = ((delta.x + cellSize.x / 2) / horizontal).toInt()
        .coerceIn(0, GridHorizontalCellCount - 1)
    val verticalCount = ((delta.y + cellSize.y / 2) / vertical).toInt()
        .coerceIn(0, GridVerticalCellCount - 1)

    return GridHorizontalCellCount * verticalCount + horizontalCount
}

private fun FreeformScope.scrollableHeight(model: FreeformModel): Float {
    return if (model.open == null) {
        0f
    } else {
        val count = model.stickies.count { (_, sticky) -> sticky.category == model.open }
        val rows = (count / 2) + 1
        val requiredHeight = spacer.y + rows * (spacer.y + cellSize.y)
        maxOf(0f, requiredHeight - size.y)
    }
}

@Composable
fun Pane(modifier: Modifier = Modifier) {
    // Set the initial model.
    var model by remember { mutableStateOf(initialModel) }
    val (dragged, setDragged) = remember { mutableStateOf(emptySet<StickyIdentifier>()) }
    val (changeCategoryOverlayStart, setChangeCategoryOverlayStart) = remember {
        mutableStateOf<Pair<StickyIdentifier, Long>?>(null)
    }

    Freeform(
        modifier
            .background(Color.StickiesFakeWhite)
            .fillMaxSize()
    ) {

        // Prepare the scroll handlers.
        var detailScroll by remember { mutableStateOf(NoScroll()) }

        // The controller in charge of letting us scroll. Makes sure we do not overscroll the
        // bounds of our views.
        val controller = rememberScrollableController(consumeScrollDelta = { delta ->
            val previous = detailScroll.amount
            val next = (detailScroll.amount - delta).coerceIn(0f, scrollableHeight(model))
            detailScroll = Scrolled(next)
            next - previous
        })

        // Our scrollable observer. It fills the background of the whole view.
        Box(
            modifier = Modifier.fillMaxSize().scrollable(Orientation.Vertical, controller),
            children = {},
        )

        // Render the category details placeholders, as needed.
        val showDetailOptions = model.isOpen && dragged.isNotEmpty()
        Placeholder(
            title = "Change category",
            asset = vectorResource(R.drawable.ic_category_action_move),
            color = animate(
                if (showDetailOptions) contentColorFor(MaterialTheme.colors.surface).copy(alpha = 0.2f)
                else Color.Transparent
            ),
            background = Color.Transparent,
            modifier = Modifier
                .offset(restOffset(0))
                .zIndex(3f)
        )
        Placeholder(
            title = "Delete forever",
            asset = vectorResource(R.drawable.ic_category_action_trash),
            color = animate(
                if (showDetailOptions) Color.StickiesNicerRed
                else Color.Transparent
            ),
            background = Color.Transparent,
            modifier = Modifier
                .offset(restOffset(GridHorizontalCellCount))
                .zIndex(3f)
        )

        // Render all the category placeholders.
        model.categories.fastForEachIndexed { index, category ->
            key(category) {
                val color = if (model.open != null) Color.StickiesFakeWhite
                else contentColorFor(color = MaterialTheme.colors.surface)
                val spring = spring<Offset>(dampingRatio = 0.85f, Spring.StiffnessLow)

                // Placeholder-specific drag information.
                val restOffset = restOffset(index)
                val (drag, setDrag) = remember { mutableStateOf(NotDragging()) }
                val position = drag.position ?: restOffset

                // Render a title that can be dragged.
                PlaceholderTitle(
                    title = category.title,
                    asset = vectorResource(category.icon),
                    color = animate(color).copy(alpha = 0.2f),
                    modifier = Modifier
                        .offset(animate(position, spring))
                        .zIndex(if (drag.isDragging) 2f else 1f),
                    longPressDragObserver = object : LongPressDragObserver {
                        override fun onDragStart() = setDrag(Dragging(position))

                        override fun onDrag(dragDistance: Offset): Offset {
                            setDrag(Dragging(position + dragDistance))
                            // Consume all the drags.
                            return dragDistance
                        }

                        override fun onStop(velocity: Offset) {
                            if (!model.isOpen) {
                                model = model.swapCategories(index, dropIndex(position))
                            }
                            setDrag(NotDragging())
                        }
                    },
                ) {
                    // Render the actual content of the category.
                    Placeholder(
                        title = category.title,
                        asset = vectorResource(category.icon),
                        color = animate(color).copy(alpha = 0.2f),
                    )
                }
            }
        }

        // Prepare pile information.
        val pileIndex = IntArray(GridVerticalCellCount * GridHorizontalCellCount) { -1 }
        val pileSize = IntArray(GridVerticalCellCount * GridHorizontalCellCount)
        for ((_, sticky) in model.stickies) {
            pileSize[sticky.category] += 1
        }

        // Iterate over all the stickies, in their position order..
        for ((_, sticky) in model.stickies.asIterable().sortedByDescending { it.value.pileIndex }) {
            key(sticky.identifier) {

                // Sticky-specific drag state persistence.
                val (drag, setDrag) = remember { mutableStateOf(NotDragging()) }

                // Piles count are incremented by one.
                pileIndex[sticky.category] += 1

                // Information related to whether the sticker is open or not.
                val isSelfOpen = model.open == sticky.category
                val isAnyOpen = model.isOpen

                // Sticky offset.
                val stickyRestOffset = when {
                    model.open == sticky.category -> detailOffset(
                        index = pileIndex[sticky.category],
                        scroll = detailScroll
                    )
                    model.isOpen -> hiddenOffset(
                        index = sticky.category,
                        open = model.open ?: 0
                    )
                    else -> restOffset(index = sticky.category)
                }
                val position = drag.position ?: stickyRestOffset

                FreeformSticky(
                    detailed = isSelfOpen,
                    bubbled = sticky.highlighted,
                    dragged = drag.isDragging,
                    offset = position,
                    title = sticky.title,
                    color = sticky.color,
                    pileIndex = pileIndex[sticky.category],
                    pileSize = pileSize[sticky.category],
                    onClick = {
                        if (isSelfOpen) {
                            detailScroll = NoScroll()
                            model = model.copy(open = null)
                        } else {
                            detailScroll = NoScroll()
                            model = model.copy(open = sticky.category)
                        }
                    },
                    onDragStarted = {
                        setDragged(dragged + sticky.identifier)
                        if (isSelfOpen || !isAnyOpen) {
                            setDrag(Dragging(position))
                        }
                    },
                    onDragStopped = {
                        setDragged(dragged - sticky.identifier)
                        setDrag(NotDragging())
                        if (!isAnyOpen) {
                            model = model.move(sticky.identifier, dropIndex(position))
                        }
                    },
                    onDragOffset = { offset ->
                        if (drag.isDragging) {
                            setDrag(Dragging(position + offset))
                            if (isSelfOpen) {
                                // 700ms overlay delay for dropping on the change category.
                                if (dropIndex(position + offset) == 0) {
                                    if (changeCategoryOverlayStart == null) {
                                        setChangeCategoryOverlayStart(
                                            sticky.identifier to System.currentTimeMillis()
                                        )
                                    } else {
                                        val delta = System.currentTimeMillis() -
                                                changeCategoryOverlayStart.second
                                        if (delta > 700L &&
                                            changeCategoryOverlayStart.first == sticky.identifier
                                        ) {
                                            setChangeCategoryOverlayStart(null)
                                            detailScroll = NoScroll()
                                            model = model.copy(open = null)
                                        }
                                    }
                                } else {
                                    // We are not overlaying.
                                    if (changeCategoryOverlayStart?.first == sticky.identifier) {
                                        setChangeCategoryOverlayStart(null)
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
 * @param onDragStarted called when a drag gesture starts on the sticky.
 * @param onDragOffset called when a drag gesture moves on the sticky.
 * @param onDragStopped called when a drag gesture stops on the sticky.
 * @param onClick called when this sticky is clicked.
 * @param modifier the [Modifier] for this composable.
 */
@Composable
private fun FreeformSticky(
    detailed: Boolean,
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
    val skipSlowdown = if (detailed || dragged) 0f else 1f
    val spring = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = StickyMaxStiffness - (stiffnessStep * pileIndex * skipSlowdown),
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
    val base = elevation.value + ((pileSize - (pileIndex + 1)) / pileSize.toFloat()) + 10f
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
                    rotationZ = animate(
                        if (detailed || dragged) 0f
                        else PileAngles[pileIndex % PileAngles.size],
                        spring,
                    ),
                    transformOrigin = TransformOrigin.Center,
                ).offset(
                    x = animate(
                        if (detailed || dragged) 0.dp
                        else PileOffsetX[pileIndex % PileOffsetX.size],
                        dpSpring,
                    ),
                    y = animate(
                        if (detailed || dragged) 0.dp
                        else PileOffsetY[pileIndex % PileOffsetY.size],
                        dpSpring,
                    ),
                )
        )
    }
}

object PaneConstants {

    // Stiffness that's given to the different springs.
    const val StickyMinStiffness = Spring.StiffnessVeryLow
    const val StickyMaxStiffness = Spring.StiffnessLow

    // Angles and offsets applied to stickies, depending on their pile index.
    val PileAngles = listOf(0f, 3f, -2f)
    val PileOffsetX = listOf(0.dp, 4.dp, (-4).dp, 3.dp)
    val PileOffsetY = listOf(0.dp, (-6).dp, (-6).dp, 2.dp)
}