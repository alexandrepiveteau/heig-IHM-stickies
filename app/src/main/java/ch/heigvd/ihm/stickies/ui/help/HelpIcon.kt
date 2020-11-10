package ch.heigvd.ihm.stickies.ui.help

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.res.vectorResource
import ch.heigvd.ihm.stickies.R

@Composable
fun HelpIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick, modifier) {
        Icon(vectorResource(R.drawable.ic_action_help),
            Modifier.drawLayer(scaleX = 1.25f, scaleY = 1.25f))
    }
}
