package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.animation.animate
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch.heigvd.ihm.stickies.ui.stickies.Sticky
import ch.heigvd.ihm.stickies.ui.stickies.StickyTextStyle

/**
 * A composable that allows for sticky content edition.
 *
 * @param text the current text for sticky edition.
 * @param onTextChange called whenever the sticky content changes.
 * @param color the color to be used as the sticky background.
 * @param modifier the [Modifier] for this composable.
 */
@Composable
fun EditSticky(
    text: String,
    onTextChange: (String) -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Sticky(modifier, animate(color)) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            activeColor = Color.Black,
            inactiveColor = Color.Black,
            backgroundColor = Color.Transparent,
            placeholder = {
                Text("What do you want to remember ?", style = StickyTextStyle)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
