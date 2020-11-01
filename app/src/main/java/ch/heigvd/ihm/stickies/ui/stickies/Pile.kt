package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.offsetPx
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.TransformOrigin
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.LongPressDragObserver
import androidx.compose.ui.gesture.longPressDragGestureFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.HapticFeedBackAmbient
import androidx.compose.ui.unit.dp
import ch.heigvd.ihm.stickies.model.Sticky
import ch.heigvd.ihm.stickies.util.fastForEachIndexedReversed

private val PileAngles = listOf(0f, 3f, 2f)
private val PileOffsetX = listOf(0.dp, 4.dp, (-4).dp)
private val PileOffsetY = listOf(0.dp, (-6).dp, (-6).dp)

@Composable
fun Pile(
    data: List<Sticky>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        val (dragged, setDragged) = remember { mutableStateOf(false) }
        val (bubbled, setBubbled) = remember { mutableStateOf(false) }
        val scale = animate(if (dragged) 1.15f else 1.0f)
        val offsetX = remember { mutableStateOf(0f) }
        val offsetY = remember { mutableStateOf(0f) }
        val count = data.size
        val stiffnessStep = (Spring.StiffnessMedium - Spring.StiffnessLow) / (count - 2)
        data.fastForEachIndexedReversed { index, sticky ->
            if (index == 0) {
                Bubble(
                    visible = bubbled,
                    Modifier
                        .offsetPx(offsetX, offsetY)
                        .drawLayer(scaleX = scale, scaleY = scale)
                ) {
                    val haptic = HapticFeedBackAmbient.current
                    Sticky(
                        sticky,
                        Modifier
                            .clickable(onClick = { setBubbled(!bubbled) }, indication = null)
                            .longPressDragGestureFilter(object : LongPressDragObserver {
                                override fun onLongPress(pxPosition: Offset) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    setDragged(true)
                                }

                                override fun onDragStart() {
                                    // Ignored.
                                }

                                override fun onStop(velocity: Offset) {
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
                val springScale = animate(scale, spring)
                Sticky(
                    data = sticky,
                    Modifier.drawLayer(
                        translationX = x,
                        translationY = y,
                        scaleX = springScale,
                        scaleY = springScale,
                        transformOrigin = TransformOrigin.Center,
                        rotationZ = PileAngles[index % PileAngles.size]
                    ).offset(
                        x = PileOffsetX[index % PileOffsetX.size],
                        y = PileOffsetY[index % PileOffsetY.size]
                    )
                )
            }
        }
    }
}