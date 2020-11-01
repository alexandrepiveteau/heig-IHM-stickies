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

enum class SelectionColor {
    Blue,
    Pink,
    Yellow,
    Orange,
}

@Composable
fun ColorPicker(
    selected: SelectionColor,
    onClick: (SelectionColor) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        CircularPill(
            color = Color(0xFFD4F1EA),
            Modifier.clickable(onClick = { onClick(SelectionColor.Blue) }, indication = null),
            filled = selected == SelectionColor.Blue,
            content = {})
        Spacer(Modifier.width(16.dp))
        CircularPill(
            color = Color(0xFFFFDDF8),
            Modifier.clickable(onClick = { onClick(SelectionColor.Pink) }, indication = null),
            filled = selected == SelectionColor.Pink,
            content = {})
        Spacer(Modifier.width(16.dp))
        CircularPill(
            color = Color(0xFFFFFFD1),
            Modifier.clickable(onClick = { onClick(SelectionColor.Yellow) }, indication = null),
            filled = selected == SelectionColor.Yellow,
            content = {})
        Spacer(Modifier.width(16.dp))
        CircularPill(
            color = Color(0xFFFFE2D1),
            Modifier.clickable(onClick = { onClick(SelectionColor.Orange) }, indication = null),
            filled = selected == SelectionColor.Orange,
            content = {})
    }
}

@Composable
@Preview
private fun ColorPickerPreview() {
    Stack(Modifier.background(Color.White).padding(16.dp)) {
        val (color, setColor) = remember { mutableStateOf(SelectionColor.Pink) }
        ColorPicker(selected = color, onClick = setColor, Modifier.align(Alignment.Center))
    }
}