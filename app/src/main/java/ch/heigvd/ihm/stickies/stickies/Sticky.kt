package ch.heigvd.ihm.stickies.stickies

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
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
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.preferredSize(256.dp)
            .clickable(onClick = onClick),
        color = color,
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
    ) {
        Box(alignment = Alignment.Center) {
            content()
            val (checked, setChecked) = remember { mutableStateOf(false) }
            Switch(checked = checked, onCheckedChange = { setChecked(!checked) })
        }
    }
}

@Composable
@Preview
fun Demo() {
    Box(Modifier.fillMaxSize(), alignment = Alignment.Center) {
        val (value, setValue) = remember { mutableStateOf(Color.Red) }
        Sticky(
            color = value,
            onClick = { setValue(Color.Black) }
        ) {
            Text("Salut", textAlign = TextAlign.Center)
        }
    }
}