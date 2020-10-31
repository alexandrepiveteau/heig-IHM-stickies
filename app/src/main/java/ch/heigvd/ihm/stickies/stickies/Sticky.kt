package ch.heigvd.ihm.stickies.stickies

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
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun Sticky(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .preferredSize(256.dp)
            .clickable(onClick = onClick),
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
}

@Composable
@Preview
fun StickyBubble() {
    Box(
        Modifier.background(Color.White, CircleShape)
            .border(8.dp, Color.Red, CircleShape)
            .size(56.dp),
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
        Sticky(
            onClick = { }
        ) {
            Text("Salut", textAlign = TextAlign.Center)
        }
    }
}