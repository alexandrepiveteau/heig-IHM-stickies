package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ch.heigvd.ihm.stickies.ui.StickiesYellow

@Composable
fun NewSticky(
    onCancel: () -> Unit,
    onNewSticky: (title: String, color: Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (color, setColor) = remember { mutableStateOf(Color.StickiesYellow) }
    val (text, setText) = remember { mutableStateOf("") }
    Row(
        modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            StickyDetails(color, setColor)
            Spacer(Modifier.height(16.dp))
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier.size(300.dp).padding(32.dp)
        ) {
            TextField(value = text, onValueChange = setText)
        }
        Button(onClick = { onNewSticky(text, color) }) {
            Text("Save")
        }
    }
}