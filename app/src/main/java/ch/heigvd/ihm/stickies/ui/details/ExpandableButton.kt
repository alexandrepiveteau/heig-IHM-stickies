package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun ExpendableButton(
    expanded: Boolean,
    onClick: (Boolean) -> Unit,
    color: Color,
    expandedText: String,
    contractedText: String,
    modifier: Modifier = Modifier,
) {
    val background = Color.White
    Column(
        modifier
            .background(background, CircleShape)
            .border(2.dp, color.copy(alpha = 0.4f), CircleShape)
            .clickable(onClick = { onClick(!expanded) }, indication = null)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Providers(AmbientContentColor provides contentColorFor(color)) {
            Text(text = if (expanded) expandedText else contractedText, style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
@Preview
private fun ExpendableButtonPreview() {
    Column(Modifier.background(Color.White).padding(16.dp)) {
        val (expanded, setExpanded) = remember { mutableStateOf(false) }
        ExpendableButton(
            expanded = expanded,
            onClick = setExpanded,
            color = Color(0x999999),
            contractedText = "More settings",
            expandedText = "Less settings",
        )
    }
}