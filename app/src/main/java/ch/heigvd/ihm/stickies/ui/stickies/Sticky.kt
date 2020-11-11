package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.ProvideTextStyle
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.GochiHand
import ch.heigvd.ihm.stickies.ui.StickiesNicerRed

private val StickyAlertTextStyle = TextStyle(
    fontSize = 16.sp,
    fontFamily = GochiHand,
    fontWeight = FontWeight.Normal,
    color = Color.StickiesNicerRed,
)

@Composable
fun Sticky(
    text: String,
    alert: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    elevation: Dp = StickyDefaultElevation,
) {
    Sticky(modifier, color, elevation) {
        Text(alert, Modifier.align(Alignment.TopStart), style = StickyAlertTextStyle)
        Text(text, Modifier.padding(vertical = 8.dp), textAlign = TextAlign.Center)
    }
}

val StickyTextStyle = TextStyle(
    fontSize = 32.sp,
    lineHeight = 48.sp,
    fontFamily = GochiHand,
    fontWeight = FontWeight.Normal,
)

@Composable
fun Sticky(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    elevation: Dp = StickyDefaultElevation,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier.preferredSize(StickySize),
        color = color,
        shape = RoundedCornerShape(8.dp),
        elevation = elevation,
    ) {
        Box(
            Modifier.padding(16.dp),
            alignment = Alignment.Center
        ) {
            ProvideTextStyle(StickyTextStyle) {
                content()
            }
            Providers(AmbientContentColor provides Color.Black.copy(alpha = 0.08f)) {
                Icon(vectorResource(R.drawable.ic_action_drag),
                    Modifier
                        .align(Alignment.BottomEnd)
                        .offset(8.dp, 8.dp))
            }
        }
    }
}

val StickyDefaultElevation = 4.dp
val StickyRaisedElevation = 8.dp

val StickySize = 280.dp
