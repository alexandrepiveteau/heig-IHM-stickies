package ch.heigvd.ihm.stickies.stickies

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun Sticky(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    highlighted: Boolean = false,
) {
    Sticky(modifier, color, highlighted) {
        Text(text, textAlign = TextAlign.Center)
    }
}

@Composable
fun Sticky(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    highlighted: Boolean = false,
    content: @Composable () -> Unit
) {
    Box {
        Surface(
            modifier = modifier
                .preferredSize(256.dp)
                .align(Alignment.Center),
            color = color,
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
        ) {
            Box(
                Modifier.padding(8.dp),
                alignment = Alignment.Center
            ) {
                content()
            }
        }
        StickyBubble(
            highlighted,
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = 28.dp, y = (-28).dp)
        )
    }
}

@Composable
fun StickyBubble(
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

@Composable
@Preview
fun Demo() {
    Box(Modifier.fillMaxSize(), alignment = Alignment.Center) {
        val (visible, setVisible) = remember { mutableStateOf(true) }
        Sticky(
            text = "Hello world",
            modifier = Modifier.clickable(onClick = { setVisible(!visible) }),
            highlighted = visible,
        )
    }
}