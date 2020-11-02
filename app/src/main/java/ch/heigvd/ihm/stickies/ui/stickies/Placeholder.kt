package ch.heigvd.ihm.stickies.ui.stickies

import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.TransformOrigin
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.Archivo
import ch.heigvd.ihm.stickies.ui.modifier.aboveOffset

private val HintTextStyle = TextStyle(
    fontFamily = Archivo,
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
)
private val ContentTextStyle = TextStyle(
    fontFamily = Archivo,
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
)

@Composable
fun Placeholder(
    title: String,
    asset: VectorAsset,
    modifier: Modifier = Modifier,
) {
    // TODO : Use context-provided instead.
    val color = Color.Black.copy(alpha = 0.2f)
    val border = Color.Black.copy(alpha = 0.1f)
    Providers(AmbientContentColor provides color) {
        Box(
            modifier = modifier
                .border(4.dp, border, RoundedCornerShape(32.dp))
                .size(256.dp),
            alignment = Alignment.Center,
        ) {
            Column(
                Modifier
                    .align(Alignment.TopStart)
                    .aboveOffset()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(Modifier.preferredWidth(8.dp))
                    Icon(asset, Modifier.preferredSize(24.dp))
                    Spacer(Modifier.preferredWidth(8.dp))
                    Text(title, style = HintTextStyle)
                }
                Spacer(Modifier.height(16.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    asset, Modifier
                        .preferredSize(64.dp)
                        .drawLayer(
                            scaleX = 3f,
                            scaleY = 3f,
                            transformOrigin = TransformOrigin.Center,
                        )
                )
                Spacer(Modifier.preferredHeight(8.dp))
                Text(title, style = ContentTextStyle)
            }
        }
    }
}

@Composable
@Preview
private fun PlaceholderPreview() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            Placeholder(
                title = "Inbox",
                asset = vectorResource(R.drawable.ic_category_inbox),
            )
        }
    }
}