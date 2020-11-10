package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.StickiesSuperGraddyStart

@Composable
fun ExpandButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonConstants.defaultOutlinedButtonColors(
            contentColor = Color.StickiesSuperGraddyStart,
        ),
        border = BorderStroke(
            width = 2.dp,
            color = Color.StickiesSuperGraddyStart.copy(alpha = 0.2f),
        ),
        elevation = null,
        shape = CircleShape,
        modifier = modifier,
    ) {
        Text(if (!expanded) "More settings" else "Less settings")
        Spacer(Modifier.width(16.dp))
        val angle = animate(if (expanded) -90f else 90f, spring(stiffness = StiffnessLow))
        Icon(
            asset = vectorResource(R.drawable.ic_action_chevron_right),
            modifier = Modifier.drawLayer(rotationZ = angle),
        )
    }
}

@Composable
@Preview
private fun ExpandButtonPreview() {
    Column(Modifier.background(Color.White).padding(16.dp)) {
        val (expanded, setExpanded) = remember { mutableStateOf(false) }
        ExpandButton(
            expanded = expanded,
            onClick = { setExpanded(!expanded) },
        )
    }
}