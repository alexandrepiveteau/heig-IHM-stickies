package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.heigvd.ihm.stickies.ui.Archivo

private val PortionTextStyle = TextStyle(
    fontFamily = Archivo,
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
)

/**
 * Represents a portion of the sticky details. Multiple portions will be displayed in a column, with
 * different settings available for each of them.
 *
 * @param title the title to be displayed at the top of the portion.
 * @param modifier the [Modifier] for this composable.
 * @param content the children of this portion.
 */
@Composable
fun Portion(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(modifier, shape = RoundedCornerShape(8.dp), elevation = 2.dp) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = PortionTextStyle)
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}