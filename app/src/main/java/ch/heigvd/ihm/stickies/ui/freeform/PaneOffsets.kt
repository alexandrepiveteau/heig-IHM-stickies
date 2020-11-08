package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.ui.geometry.Offset
import ch.heigvd.ihm.stickies.ui.Model

/**
 * Calculates the offset to be applied to a cell at a certain grid index to hide it from the
 * category detail view.
 *
 * @param index the grid index of the item to hide.
 *
 * @return the [Offset] at which the items will be placed when hidden.
 */
fun FreeformScope.hidden(
    index: Int,
    open: Int,
): Offset {
    return rest(index)
        .plus(
            Offset(
                x = 0f,
                y = if (open < (FreeformConstants.GridHorizontalCellCount *
                            FreeformConstants.GridVerticalCellCount) / 2
                ) size.y
                else -size.y
            )
        )
}

/**
 * Calculates the offset to be applied to a cell that is shown in the detail view.
 *
 * @param index the grid index of the item to show.
 * @param scroll how much scroll amount there currently is.
 *
 * @return the [Offset] at which the item at the index-th position should be placed.
 */
fun FreeformScope.detail(
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
 * Calculates the drop index of an item for the category detail. If the element is dropped between
 * some existing cards, it will simply be ignored.
 *
 * TODO : Make sure that drops after the list works no matter what.
 *
 * @param scroll how much scroll amount there currently is.
 * @param position the [Offset] at which the item is dropped.
 *
 * @return null if no drop target can be found, or the drop index (starting at 0) otherwise.
 */
fun FreeformScope.dropIndexDetail(
    scroll: ScrollState,
    position: Offset,
): Int? {
    val dropPosition = Offset(
        x = position.x + cellSize.x / 2,
        y = position.y + scroll.amount + cellSize.y / 2,
    ) - origin
    val col1Start = 2 * spacer.x + cellSize.x
    val col1End = col1Start + cellSize.x
    val col2Start = col1End + spacer.x
    val col2End = col2Start + cellSize.x

    val column = when (dropPosition.x) {
        in col1Start..col1End -> 1
        in col2Start..col2End -> 2
        else -> {
            return null
        }
    }

    // Incremental row calculation. We could do that in O(1) too.
    var row = 1
    var vertical = 0.5f * spacer.y
    while (true) {
        if (dropPosition.y < vertical) {
            return null
        } else if (dropPosition.y < vertical + cellSize.y) {
            break
        } else {
            vertical += cellSize.y + spacer.y
            row++
        }
    }

    return (((row - 1) * 2) + column) - 1
}

/**
 * Calculates the offset at which an item at a certain index should be positioned to be at rest.
 *
 * @param index the index for which we want the rest offset.
 *
 * @return the [Offset] at which the item should be put at rest.
 */
fun FreeformScope.rest(
    index: Int,
): Offset {
    val horizontalCount = index % FreeformConstants.GridHorizontalCellCount
    val verticalCount = (index - horizontalCount) / FreeformConstants.GridHorizontalCellCount
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
fun FreeformScope.dropIndex(
    position: Offset,
): Int {
    // TODO : This implementation completely ignores the spacings. Maybe this is something we'll
    //        actually want to take into account.
    val delta = position - origin
    val horizontal = size.x / FreeformConstants.GridHorizontalCellCount
    val vertical = size.y / FreeformConstants.GridVerticalCellCount

    val horizontalCount = ((delta.x + cellSize.x / 2) / horizontal).toInt()
        .coerceIn(0, FreeformConstants.GridHorizontalCellCount - 1)
    val verticalCount = ((delta.y + cellSize.y / 2) / vertical).toInt()
        .coerceIn(0, FreeformConstants.GridVerticalCellCount - 1)

    return FreeformConstants.GridHorizontalCellCount * verticalCount + horizontalCount
}

/**
 * Calculates the amount of scroll that can be performed for a certain [Model]. This
 * considers the state of the model, and especially if a category can be opened (as well as how
 * many elements are in that category !).
 *
 * @return a pixel scroll amount that can not be exceeded.
 */
fun FreeformScope.scrollableHeight(model: Model): Float {
    return if (!model.categoryOpen) {
        0f
    } else {
        val count = model.stickies.count { (_, sticky) ->
            sticky.category == model.categoryOpenIndex
        }
        val rows = (count / 2) + 1
        val requiredHeight = spacer.y + rows * (spacer.y + cellSize.y)
        maxOf(0f, requiredHeight - size.y)
    }
}