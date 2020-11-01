package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ch.heigvd.ihm.stickies.model.Sticky

@Composable
fun Sticky(
    data: Sticky,
    modifier: Modifier = Modifier,
) {
    Sticky(
        text = data.title,
        highlighted = data.highlighted,
        color = data.color,
        modifier = modifier,
    )
}

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