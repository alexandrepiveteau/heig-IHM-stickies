package ch.heigvd.ihm.stickies.ui.help

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.StickiesSuperGraddyStart
import ch.heigvd.ihm.stickies.ui.material.BigGradientButton

@Composable
fun HelpOverlay(
    onCancel: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier,
        Arrangement.spacedBy(64.dp, Alignment.CenterVertically),
        Alignment.CenterHorizontally,
    ) {
        Card(Modifier.tapGestureFilter {  }.width(560.dp)) {
            Column(Modifier.padding(32.dp), Arrangement.spacedBy(32.dp)) {
                Text(
                    "Tips & Tricks",
                    color = Color.StickiesSuperGraddyStart,
                    style = MaterialTheme.typography.h5,
                )
                // Opening a category.
                HelpFeature(
                    title = "Navigating in the app",
                    description = "This app uses stickies stacks to navigate. Just click on one of them to open it up and see all of its contents !",
                )
                // Dragging stickies around.
                HelpFeature(
                    title = "Moving stickies around",
                    description = "Drag and drop stickies across stacks to reorder them and re-categorize them. You can move multiple stickies at once.",
                )
                // Moving categories.
                HelpFeature(
                    title = "Re-ordering categories",
                    description = "Stacks of stickies can be re-organized by dragging and dropping them across the canvas. Hold and press their title to do that.",
                )
            }
        }
        BigGradientButton(
            onClick = onCancel,
            icon = vectorResource(R.drawable.ic_action_close),
            title = "Close",
            from = Color.Black.copy(alpha = 0.2f), to = Color.Black.copy(alpha = 0.3f),
        )
    }
}

@Composable
fun HelpFeature(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier, Arrangement.spacedBy(16.dp)) {
        Text(title, style = MaterialTheme.typography.h6)
        Text(description, style = MaterialTheme.typography.body2)
    }
}
