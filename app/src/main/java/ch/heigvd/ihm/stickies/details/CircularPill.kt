package ch.heigvd.ihm.stickies.details

import androidx.compose.foundation.ContentColorAmbient
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun CircularPill(
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val border = remember(color) { color.copy(alpha = 0.1f) }
    val background = remember(color) { color.copy(alpha = 0.05f) }
    Column(
        modifier
            .preferredSize(76.dp)
            .background(background, CircleShape)
            .border(4.dp, border, CircleShape),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Providers(ContentColorAmbient provides contentColorFor(color)) {
            content()
        }
    }
}

@Composable
@Preview
private fun CircularPillPreview() {
    Stack(Modifier.background(Color.White).padding(16.dp)) {
        CircularPill(color = Color.Red) {
            Text("Tue", style = MaterialTheme.typography.subtitle1)
            Text("•", style = MaterialTheme.typography.subtitle2)
        }
    }
}