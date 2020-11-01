package ch.heigvd.ihm.stickies.ui

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
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.LongPressDragObserver
import androidx.compose.ui.gesture.longPressDragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.HapticFeedBackAmbient
import androidx.compose.ui.unit.dp
import ch.heigvd.ihm.stickies.model.Sticky
import ch.heigvd.ihm.stickies.ui.modifier.offsetPx
import ch.heigvd.ihm.stickies.ui.stickies.Bubble
import ch.heigvd.ihm.stickies.ui.stickies.Sticky
import ch.heigvd.ihm.stickies.ui.stickies.StickyDefaultElevation
import ch.heigvd.ihm.stickies.ui.stickies.StickyRaisedElevation
import ch.heigvd.ihm.stickies.util.fastForEachIndexedReversed
import kotlinx.coroutines.flow.flow
import kotlin.math.sign

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
) {
    WithConstraints(modifier.fillMaxSize()) {
        val width = with(DensityAmbient.current) { maxWidth.toPx() }
        val height = with(DensityAmbient.current) { maxHeight.toPx() }
        val size = with(DensityAmbient.current) { StickySize.toPx() }
        val spacerY = (height - (2 * size)) / 3
        val startY = (size - height) / 2

        val firstInitialOffset = Offset(x = 0f, y = startY + spacerY)
        val secondInitialOffset = Offset(x = 0f, y = startY + spacerY + size + spacerY)

        val (oneRestOffset, setOneRestOffset) = remember { mutableStateOf(firstInitialOffset) }
        val (twoRestOffset, setTwoRestOffset) = remember { mutableStateOf(secondInitialOffset) }

        val (oneOffset, setOneOffset) = remember { mutableStateOf(firstInitialOffset) }
        val (twoOffset, setTwoOffset) = remember { mutableStateOf(secondInitialOffset) }

        FreeformPile(
            restOffset = oneRestOffset,
            stickies = ExamplePileA().value,
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
            stickies = ExamplePileB().value,
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
    val size = with(DensityAmbient.current) { StickySize.toPx() }
    ProvideEmphasis(AmbientEmphasisLevels.current.disabled) {
        Column {
            Text("Size : $width x $height")
            Text("Sticky size : $size x $size")
        }
    }
}

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

// TODO : REMOVE THIS EXAMPLE DATA

@Composable
fun ExamplePileA(): State<List<Sticky>> {
    val flow = flow {
        emit(
            listOf(
                Sticky(11, Color.StickiesYellow, "Take Medor to the vet", false),
                Sticky(12, Color.StickiesOrange, "Buy some cat food", false)
            )
        )
    }
    val data = remember { flow }
    return data.collectAsState(emptyList())
}

@Composable
fun ExamplePileB(): State<List<Sticky>> {
    val flow = flow {
        var highlighted = false
        while (true) {
            emit(
                listOf(
                    Sticky(21, Color.StickiesPink, "Dentist at 10 am", highlighted),
                    Sticky(22, Color.StickiesBlue, "Take some Aspirin", false),
                    Sticky(23, Color.StickiesYellow, "Call my pharmacist", false)
                )
            )
            kotlinx.coroutines.delay(5000)
            highlighted = !highlighted
        }
    }
    val data = remember { flow }
    return data.collectAsState(emptyList())
}