package ch.heigvd.ihm.stickies.ui.material

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.animate
import androidx.compose.animation.core.*
import androidx.compose.animation.core.AnimationConstants.Infinite
import androidx.compose.animation.transition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.onGloballyPositioned
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FirstColor = ColorPropKey()
private val SecondColor = ColorPropKey()
private val ThirdColor = ColorPropKey()
private val FourthColor = ColorPropKey()

private enum class State {
    Start,
    End,
}

private fun gradient(from: Color, to: Color): TransitionDefinition<State> = transitionDefinition {
    state(State.Start) {
        this[FirstColor] = from
        this[SecondColor] = to
        this[ThirdColor] = to
        this[FourthColor] = from
    }
    state(State.End) {
        this[FirstColor] = to
        this[SecondColor] = from
        this[ThirdColor] = from
        this[FourthColor] = to
    }
    transition {
        FirstColor using repeatable(
            animation = tween(3 * 1000),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        SecondColor using repeatable(
            animation = tween(4 * 1000),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        ThirdColor using repeatable(
            animation = tween(3 * 1000),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse,
        )
        FourthColor using repeatable(
            animation = tween(5 * 1000),
            iterations = Infinite,
            repeatMode = RepeatMode.Reverse,
        )
    }
}

/**
 * An alternative to [Button] that uses an animated gradient on its inner border. The gradient
 * color scheme is Smurf- and Flamingo-based.
 *
 * @param onClick the callback called when the button is clicked.
 * @param from the color that we start the gradient with.
 * @param to the color that we end the gradient with.
 * @param modifier the modifier for the button.
 * @param borderSize the width of the gradient border.
 * @param content the inner contents of the button.
 */
@Composable
fun GradientButton(
    onClick: () -> Unit,
    from: Color, to: Color,
    modifier: Modifier = Modifier,
    borderSize: Dp = 2.dp,
    content: @Composable RowScope.() -> Unit,
) {
    // On first composition, we don't have access to our bounds. Use a best-effort guess.
    val end = with(DensityAmbient.current) { 300.dp.toPx() }
    var bounds by remember { mutableStateOf(Rect(Offset(0f, 0f), Size(end, end / 2))) }
    val values = transition(
        definition = remember { gradient(from, to) }, initState = State.Start,
        toState = State.End
    )
    val brush = LinearGradient(
        0.0f to values[FirstColor],
        0.2f to values[SecondColor],
        0.5f to values[ThirdColor],
        0.85f to values[FourthColor],
        // Animated bounds changes.
        startX = animate(bounds.left * 2),
        startY = animate(bounds.top * 2),
        endX = animate(bounds.right * 2),
        endY = animate(bounds.bottom * 2),
        tileMode = TileMode.Mirror,
    )
    val stroke = BorderStroke(
        width = borderSize,
        brush = brush
    )
    Button(
        onClick = onClick,
        colors = ButtonConstants.defaultButtonColors(
            contentColor = Color.Black,
            backgroundColor = Color.White,
        ),
        border = stroke,
        shape = RoundedCornerShape(50),
        modifier = modifier.onGloballyPositioned { bounds = it.boundsInParent },
        content = content,
    )
}

/**
 * An alternative to [Button] that uses an animated gradient on its inner border.
 *
 * @param value the string inside the button
 * @param onClick the callback called when the button is clicked.
 * @param from the color that we start the gradient with.
 * @param to the color that we end the gradient with.
 * @param modifier the modifier for the button.
 */
@Composable
fun GradientButton(
    value: String,
    from: Color, to: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GradientButton(
        onClick = onClick,
        from = from, to = to,
        modifier = modifier
            .preferredHeight(56.dp),
    ) {
        Text(
            text = value,
            fontSize = 14.sp
        )
    }
}
