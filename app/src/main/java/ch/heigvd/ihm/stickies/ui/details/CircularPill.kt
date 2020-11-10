package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.animation.animate
import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun CircularPill(
    color: Color,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val border = animate(color
        .copy(alpha = 0.4f)
        .compositeOver(Color.Black.copy(alpha = 0.2f)))
    val background = color
        .copy(alpha = animate(if (filled) 0.8f else 0.0f))
        .compositeOver(Color.Black.copy(alpha = animate(if (filled) 0.2f else 0f)))
    Column(
        modifier
            .preferredSize(76.dp)
            .background(background, CircleShape)
            .border(4.dp, border, CircleShape),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Providers(AmbientContentColor provides contentColorFor(color)) {
            content()
        }
    }
}

@Composable
@Preview
private fun CircularPillPreview() {
    Column(Modifier.background(Color.White).padding(16.dp)) {
        CircularPill(color = Color.Red) {
            Text("Tue", style = MaterialTheme.typography.subtitle1)
            Text("•", style = MaterialTheme.typography.subtitle2)
        }
    }
}