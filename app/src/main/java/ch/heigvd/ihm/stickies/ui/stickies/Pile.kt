package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offsetPx
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import ch.heigvd.ihm.stickies.model.Sticky
import ch.heigvd.ihm.stickies.util.fastForEachIndexedReversed

@Composable
fun Pile(
    data: List<Sticky>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        val (dragged, setDragged) = remember { mutableStateOf(false) }
        val offsetX = remember { mutableStateOf(0f) }
        val offsetY = remember { mutableStateOf(0f) }
        val count = data.size
        val stiffnessStep = (Spring.StiffnessMedium - Spring.StiffnessLow) / (count - 2)
        data.fastForEachIndexedReversed { index, sticky ->
            if (index == 0) {
                Bubble(
                    visible = dragged,
                    Modifier.offsetPx(offsetX, offsetY)
                ) {
                    Sticky(
                        sticky,
                        Modifier
                            .dragGestureFilter(
                                canDrag = { true },
                                dragObserver = object : DragObserver {
                                    override fun onStart(downPosition: Offset) {
                                        super.onStart(downPosition)
                                        setDragged(true)
                                    }

                                    override fun onStop(velocity: Offset) {
                                        super.onStop(velocity)
                                        setDragged(false)
                                    }

                                    override fun onDrag(dragDistance: Offset): Offset {
                                        offsetX.value = (offsetX.value + dragDistance.x)
                                        offsetY.value = (offsetY.value + dragDistance.y)
                                        return super.onDrag(dragDistance)
                                    }
                                }),
                    )
                }
            } else {
                val spring = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium - (stiffnessStep * (index - 1)),
                    visibilityThreshold = 0.01f,
                )
                val x = animate(offsetX.value, spring)
                val y = animate(offsetY.value, spring)
                Sticky(
                    data = sticky,
                    Modifier.drawLayer(
                        translationX = x,
                        translationY = y,
                    )
                )
            }
        }
    }
}