package ch.heigvd.ihm.stickies.stickies

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Bubble(highlighted) {
        Sticky(modifier, color) {
            Text(text, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun Sticky(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .preferredSize(256.dp),
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