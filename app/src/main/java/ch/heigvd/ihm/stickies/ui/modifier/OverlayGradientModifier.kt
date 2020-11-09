package ch.heigvd.ihm.stickies.ui.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.ContentDrawScope
import androidx.compose.ui.DrawModifier
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

/**
 * Draws a vertical gradient overlay on top of the current composable.
 */
fun Modifier.drawVerticalOverlay() = this then OverlayGradientModifier()

/**
 * A draw modifier that draws a gradient on top of some content, such that the center of the content
 * remains visible.
 */
private class OverlayGradientModifier : DrawModifier {

    override fun ContentDrawScope.draw() {
        drawContent()
        val gradient = LinearGradient(
            colors = listOf(Color.White, Color.Transparent, Color.White),
            startX = center.x,
            startY = 0f,
            endX = center.x,
            endY = size.height,
        )
        drawRect(gradient)
    }
}

@Preview
@Composable
private fun OverlayGradientPreview() {
    Box(Modifier.drawVerticalOverlay()) {
        Spacer(Modifier.size(400.dp).background(Color.Red))
    }
}