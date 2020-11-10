package ch.heigvd.ihm.stickies.ui.material

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.heigvd.ihm.stickies.ui.Archivo

private val BigGradientButtonTextStyle = TextStyle(
    fontFamily = Archivo,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
)

/**
 * An composable that uses the [GradientButton] and displays and icon, a title and has a gradient
 * border.
 *
 * @param onClick the listener when the button is clicked.
 * @param icon the icon to display.
 * @param title the title to display.
 * @param from the start color from the gradient.
 * @param to the end color from the gradient.
 * @param modifier the [Modifier] for this composable.
 */
@Composable
fun BigGradientButton(
    onClick: () -> Unit,
    icon: VectorAsset,
    title: String,
    from: Color, to: Color,
    modifier: Modifier = Modifier,
) {
    GradientButton(
        onClick = onClick,
        from = from, to = to,
        borderSize = 4.dp,
        modifier = modifier.height(64.dp),
    ) {
        Spacer(Modifier.width(16.dp))
        // TODO : Fix this icon scaling.
        Icon(icon, Modifier.drawLayer(scaleX = 1.25f, scaleY = 1.25f))
        Spacer(Modifier.width(16.dp))
        Text(title, style = BigGradientButtonTextStyle)
        Spacer(Modifier.width(16.dp))
    }
}
