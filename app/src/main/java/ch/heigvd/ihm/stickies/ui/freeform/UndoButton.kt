package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.Archivo
import ch.heigvd.ihm.stickies.ui.StickiesSuperGraddyStart

private val UndoButtonTextStyle = TextStyle(
    fontFamily = Archivo,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
)

@Composable
fun UndoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        colors = ButtonConstants.defaultOutlinedButtonColors(
            contentColor = Color.StickiesSuperGraddyStart,
        ),
        shape = CircleShape,
        border = BorderStroke(4.dp, Color.StickiesSuperGraddyStart),
    ) {
        Spacer(Modifier.width(16.dp))
        // TODO : Fix this icon scaling.
        Providers(AmbientContentColor provides Color.StickiesSuperGraddyStart) {
            Icon(
                vectorResource(R.drawable.ic_action_undo),
                Modifier.drawLayer(scaleX = 1.25f, scaleY = 1.25f)
            )
            Spacer(Modifier.width(16.dp))
            Providers(AmbientContentColor provides Color.Black) {
                Text("Undo this action", style = UndoButtonTextStyle)
            }
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Preview
@Composable
private fun UndoButtonPreview() {
    UndoButton(
        onClick = {},
    )
}