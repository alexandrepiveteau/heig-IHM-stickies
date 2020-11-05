package ch.heigvd.ihm.stickies.ui.modifier

import androidx.compose.ui.LayoutModifier
import androidx.compose.ui.Measurable
import androidx.compose.ui.MeasureScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints

/**
 * A [LayoutModifier] that displays an item right above its standard top line. This can be used to
 * decorate a view, when combined with a standard position at the top start.
 */
fun Modifier.aboveOffset(): Modifier = this.then(AboveOffset())

private class AboveOffset : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureScope.MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.place(0, -placeable.height)
        }
    }
}