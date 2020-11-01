package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.foundation.ProvideTextStyle
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.heigvd.ihm.stickies.model.Sticky
import ch.heigvd.ihm.stickies.ui.GochiHand

@Composable
fun Sticky(
    data: Sticky,
    modifier: Modifier = Modifier,
) {
    Bubble(visible = data.highlighted) {
        Sticky(
            text = data.title,
            color = data.color,
            modifier = modifier,
        )
    }
}

@Composable
fun Sticky(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
) {
    Sticky(modifier, color) {
        Text(text, textAlign = TextAlign.Center)
    }
}

private val StickyFont = TextStyle(
    fontSize = 36.sp,
    lineHeight = 48.sp,
    fontFamily = GochiHand,
    fontWeight = FontWeight.Normal,
)

@Composable
fun Sticky(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .preferredSize(256.dp),
        color = color,
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
    ) {
        Box(
            Modifier.padding(8.dp),
            alignment = Alignment.Center
        ) {
            ProvideTextStyle(StickyFont) {
                content()
            }
        }
    }
}