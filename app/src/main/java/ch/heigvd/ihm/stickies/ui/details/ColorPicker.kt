package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.ui.*

private val AvailableColors = listOf(
    Color.StickiesBlue,
    Color.StickiesPink,
    Color.StickiesYellow,
    Color.StickiesOrange,
    Color.StickiesGreen,
)

@Composable
fun ColorPicker(
    selected: Color,
    onClick: (Color) -> Unit,
    modifier: Modifier = Modifier,
    choices: List<Color> = AvailableColors,
) {
    Row(modifier, Arrangement.spacedBy(16.dp)) {
        for (color in choices) {
            CircularPill(
                color = color,
                modifier = Modifier.clickable(
                    onClick = { onClick(color) },
                    indication = null,
                ),
                filled = selected == color,
                content = {},
            )
        }
    }
}

@Composable
@Preview
private fun ColorPickerPreview() {
    Box(Modifier.background(Color.White).padding(16.dp)) {
        val (color, setColor) = remember { mutableStateOf(Color.StickiesGreen) }
        ColorPicker(selected = color, onClick = setColor, Modifier.align(Alignment.Center))
    }
}
