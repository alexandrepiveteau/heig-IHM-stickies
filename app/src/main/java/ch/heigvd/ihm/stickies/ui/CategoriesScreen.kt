package ch.heigvd.ihm.stickies.ui

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.ProvideEmphasis
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import ch.heigvd.ihm.stickies.ui.modifier.offsetPx
import ch.heigvd.ihm.stickies.ui.stickies.Bubble
import ch.heigvd.ihm.stickies.ui.stickies.Sticky
import kotlin.math.sign

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
) {
    WithConstraints(modifier.fillMaxSize()) {
        val width = with(DensityAmbient.current) { maxWidth.toPx() }
        val height = with(DensityAmbient.current) { maxHeight.toPx() }
        val size = with(DensityAmbient.current) { 256.dp.toPx() }
        val spacerY = (height - (2 * size)) / 3
        val startY = (size - height) / 2

        val firstInitialOffset = Offset(x = 0f, y = startY + spacerY)
        val secondInitialOffset = Offset(x = 0f, y = startY + spacerY + size + spacerY)

        val (oneRestOffset, setOneRestOffset) = remember { mutableStateOf(firstInitialOffset) }
        val (twoRestOffset, setTwoRestOffset) = remember { mutableStateOf(secondInitialOffset) }

        val (oneOffset, setOneOffset) = remember { mutableStateOf(firstInitialOffset) }
        val (twoOffset, setTwoOffset) = remember { mutableStateOf(secondInitialOffset) }

        Debug()
        FreeformPile(
            restOffset = oneRestOffset,
            onDrag = setOneOffset,
            onDrop = {
                if (sign(oneOffset.y) != sign(oneRestOffset.y)) {
                    setOneRestOffset(twoRestOffset)
                    setTwoRestOffset(oneRestOffset)
                }
            },
        )
        FreeformPile(
            restOffset = twoRestOffset,
            onDrag = setTwoOffset,
            onDrop = {
                if (sign(twoOffset.y) != sign(twoRestOffset.y)) {
                    setOneRestOffset(twoRestOffset)
                    setTwoRestOffset(oneRestOffset)
                }
            },
        )
    }
}

@Composable
private fun WithConstraintsScope.Debug() {
    val width = with(DensityAmbient.current) { maxWidth.toPx() }
    val height = with(DensityAmbient.current) { maxHeight.toPx() }
    val size = with(DensityAmbient.current) { 256.dp.toPx() }
    ProvideEmphasis(AmbientEmphasisLevels.current.disabled) {
        Column {
            Text("Size : $width x $height")
            Text("Sticky size : $size x $size")
        }
    }
}

@Composable
private inline fun FreeformPile(
    restOffset: Offset,
    crossinline onDrag: (absolute: Offset) -> Unit,
    crossinline onDrop: () -> Unit,
) {
    val (dragged, setDragged) = remember { mutableStateOf(false) }
    val (dragOffset, setDragOffset) = remember { mutableStateOf(Offset.Zero) }

    FreeformSticky(
        bubbled = false,
        dragged = dragged,
        offset = restOffset + dragOffset,
        title = "This is a test",
        color = Color.StickiesPink,
        pileIndex = 2,
        pileSize = 3,
    )
    FreeformSticky(
        bubbled = false,
        dragged = dragged,
        offset = restOffset + dragOffset,
        title = "World",
        color = Color.StickiesOrange,
        pileIndex = 1,
        pileSize = 3,
    )
    FreeformSticky(
        bubbled = false,
        dragged = dragged,
        offset = restOffset + dragOffset,
        title = "Hello",
        color = Color.StickiesYellow,
        pileIndex = 0,
        pileSize = 3,
        onDragStarted = { setDragged(true) },
        onDragOffset = { delta ->
            setDragOffset(dragOffset + delta)
            onDrag(dragOffset + delta)
        },
        onDragStopped = {
            setDragged(false)
            setDragOffset(Offset.Zero)
            onDrop()
        },
    )
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
    val scale = animate(if (dragged) 1.15f else 1.0f, spring)

    Bubble(
        visible = bubbled,
        modifier
            .offsetPx(animate(offset.x, spring), animate(offset.y, spring))
            .drawLayer(scaleX = scale, scaleY = scale)
    ) {
        Sticky(
            text = title,
            color = color,
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

// Angles and offsets applied to stickies, depending on their pile index.
private val PileAngles = listOf(0f, 3f, 2f)
private val PileOffsetX = listOf(0.dp, 4.dp, (-4).dp)
private val PileOffsetY = listOf(0.dp, (-6).dp, (-6).dp)