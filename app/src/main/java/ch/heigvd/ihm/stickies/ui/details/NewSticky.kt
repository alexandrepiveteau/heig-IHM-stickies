package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
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

    StickyDetails(
        color = color,
        onColorChange = setColor,
        text = text,
        onTextChange = setText,
        modifier = modifier,
    ) {
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
}
