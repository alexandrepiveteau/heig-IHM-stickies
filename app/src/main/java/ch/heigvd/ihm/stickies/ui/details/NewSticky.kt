package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.StickiesSuperGraddyEnd
import ch.heigvd.ihm.stickies.ui.StickiesSuperGraddyStart
import ch.heigvd.ihm.stickies.ui.StickiesYellow
import ch.heigvd.ihm.stickies.ui.material.BigGradientButton

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
        Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        Alignment.CenterVertically,
    ) {
        StickyDetails(color, setColor) {
            BigGradientButton(
                onClick = { onCancel() },
                from = Color.Black.copy(alpha = 0.2f), to = Color.Black.copy(alpha = 0.3f),
                icon = vectorResource(R.drawable.ic_category_action_trash),
                title = "Cancel",
            )
            BigGradientButton(
                onClick = { onNewSticky(text, color) },
                from = Color.StickiesSuperGraddyStart, to = Color.StickiesSuperGraddyEnd,
                icon = vectorResource(R.drawable.ic_action_save),
                title = "Save",
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier.size(300.dp).padding(32.dp)
        ) {
            TextField(value = text, onValueChange = setText)
        }
    }
}
