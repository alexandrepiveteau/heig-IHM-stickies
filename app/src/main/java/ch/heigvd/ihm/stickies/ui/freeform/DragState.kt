package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.ui.geometry.Offset

/**
 * A sealed class representing the different states that one can be in when dragging.
 *
 * TODO (performance) : Switch to an inline class.
 */
sealed class DragState {

    /**
     * This sticky is being dragged, with a certain offset from its starting position. The sticky
     * should be dragged individually, not as a group.
     *
     * @param dragOffset how much offset there is for this sticky.
     */
    data class DraggingSingle(val dragOffset: Offset = Offset.Zero) : DragState()

    /**
     * This sticky is being dragged, and it's "holding" its whole pile ot stickies behind it. The
     * sticky drags the whole group.
     *
     * @param dragOffset how much offset these is for this sticky group.
     */
    data class DraggingPile(val dragOffset: Offset = Offset.Zero) : DragState()

    /**
     * This sticky is not being dragged.
     */
    object NotDragging : DragState()
}