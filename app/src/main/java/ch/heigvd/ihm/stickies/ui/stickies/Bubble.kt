package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.ui.GochiHand

@Composable
fun Bubble(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            content()
        }
        StickyBubble(
            visible,
            Modifier.align(Alignment.TopEnd)
                .offset(x = 28.dp, y = (-28).dp)
        )
    }
}

private val QuestionMarkTextStyle = TextStyle(
    fontSize = 44.sp,
    lineHeight = 48.sp,
    fontFamily = GochiHand,
    fontWeight = FontWeight.Normal,
)

@Composable
private fun StickyBubble(
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    val scale = animate(
        if (visible) 1f else 0f, spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
            visibilityThreshold = 0.01f,
        )
    )
    Box(
        modifier
            .drawLayer(scaleX = maxOf(scale, 0f), scaleY = maxOf(scale, 0f))
            .size(56.dp)
            .background(Color.White, CircleShape)
            .border(8.dp, Color.Red, CircleShape)
            .drawShadow(4.dp, CircleShape),
        alignment = Alignment.Center,
    ) {
        Text(
            "!",
            modifier = Modifier.padding(top = 6.dp),
            style = QuestionMarkTextStyle,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview
private fun StickyBubblePreview() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = Color.Blue) {
            StickyBubble(visible = true)
        }
    }
}