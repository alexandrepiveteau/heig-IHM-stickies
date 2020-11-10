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
import ch.heigvd.ihm.stickies.ui.Sticky
import ch.heigvd.ihm.stickies.ui.material.BigGradientButton

@Composable
fun EditStickyOverlay(
    sticky: Sticky,
    onCancel: () -> Unit,
    onSave: (title: String, color: Color, alert: Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (color, setColor) = remember { mutableStateOf(sticky.color) }
    val (text, setText) = remember { mutableStateOf(sticky.title) }
    val (alert, setAlert) = remember { mutableStateOf(sticky.alert) }

    StickyDetails(
        color = color,
        text = text,
        alert = alert,
        onColorChange = setColor,
        onTextChange = setText,
        onAlertChange = setAlert,
        modifier = modifier,
    ) {
        BigGradientButton(
            onClick = { onCancel() },
            from = Color.Black.copy(alpha = 0.2f), to = Color.Black.copy(alpha = 0.3f),
            icon = vectorResource(R.drawable.ic_category_action_trash),
            title = "Cancel",
        )
        BigGradientButton(
            onClick = { onSave(text, color, alert) },
            icon = vectorResource(R.drawable.ic_action_save),
            title = "Save",
            from = Color.StickiesSuperGraddyStart,
            to = Color.StickiesSuperGraddyEnd,
        )
    }
}
