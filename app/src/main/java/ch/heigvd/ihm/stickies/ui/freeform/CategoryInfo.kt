package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import ch.heigvd.ihm.stickies.ui.StickiesFakeWhite
import ch.heigvd.ihm.stickies.ui.stickies.StickySize

private val TitleTextStyle = TextStyle(
    fontFamily = Archivo,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
)

private val InfoTextStyle = TextStyle(
    fontFamily = Archivo,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
)

private val BackButtonTextStyle = TextStyle(
    fontFamily = Archivo,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CategoryInfo(
    visible: Boolean,
    title: String,
    icon: VectorAsset,
    onEditClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProvideEmphasis(AmbientEmphasisLevels.current.disabled) {
        if (visible) {
            Column(
                modifier
                    .preferredWidth(StickySize)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, Modifier.drawLayer(scaleY = 1.5f, scaleX = 1.5f))
                    Spacer(Modifier.width(32.dp))
                    CategoryTitle(title, onClick = onEditClick)
                }
                Spacer(Modifier.weight(1f, true))
                Text(
                    "To delete or move a Stickie, hold and drag it with your finger.",
                    style = InfoTextStyle,
                )
                Spacer(Modifier.weight(1f, true))
                BackButton(onClick = {
                    if (visible) onBack()
                })
            }
        }
    }
}

@Composable
private fun CategoryTitle(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .clip(CircleShape)
            .clickable(onClick = onClick, indication = RippleIndication())
            .border(2.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        Arrangement.SpaceAround,
        Alignment.CenterVertically,
    )
    {
        Text(text, style = TitleTextStyle)
        Spacer(Modifier.width(16.dp))
        Icon(vectorResource(R.drawable.ic_action_edit))
    }
}

@Composable
private fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonConstants.defaultOutlinedButtonColors(
            backgroundColor = Color.StickiesFakeWhite,
            contentColor = Color.Black.copy(alpha = 0.4f),
        ),
        border = BorderStroke(4.dp, Color.Black.copy(alpha = 0.4f)),
        shape = CircleShape,
        modifier = modifier.height(64.dp)) {
        Spacer(Modifier.width(16.dp))
        Icon(
            vectorResource(R.drawable.ic_action_back),
            Modifier.drawLayer(
                scaleX = 1.25f,
                scaleY = 1.25f,
            ),
        )
        Spacer(Modifier.width(32.dp))
        Text("Back to Home", style = BackButtonTextStyle)
        Spacer(Modifier.width(16.dp))
    }
}

// PREVIEWS

@Composable
@Preview(showBackground = true)
private fun CategoryInfoPreview() {
    CategoryInfo(
        visible = true,
        title = "Groceries",
        icon = vectorResource(R.drawable.ic_category_basket),
        onEditClick = {},
        onBack = {},
    )
}