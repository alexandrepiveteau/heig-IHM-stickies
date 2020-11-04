@file:Suppress("FunctionName")

package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.ui.geometry.Offset

/**
 * A sealed class representing the different states that one can be in when dragging.
 *
 * @param offset the [Offset] that is applied to the state. null if not dragging.
 */
inline class DragState(val offset: Offset?)

/**
 * Returns true if this [DragState] is actually dragging.
 */
val DragState.isDragging: Boolean get() = offset != null

/**
 * Creates a new [DragState] with an [Offset] that indicates that a drag is occurring.
 */
fun Dragging(offset: Offset): DragState = (DragState(offset))

/**
 * Creates a new [DragState] with no [Offset] that indicates that no drag is occurring.
 */
fun NotDragging(): DragState = DragState(null)