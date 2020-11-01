package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
            .background(Color.White, CircleShape)
            .border(8.dp, Color.Red, CircleShape)
            .size(56.dp)
            .drawShadow(4.dp, CircleShape),
        alignment = Alignment.Center,
    ) {
        Text(
            "!",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
        )
    }
}