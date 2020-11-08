package ch.heigvd.ihm.stickies.ui.modifier

import androidx.compose.ui.LayoutModifier
import androidx.compose.ui.Measurable
import androidx.compose.ui.MeasureScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

/**
 * Offset the content by [offset] px. The offsets can be positive as well as non-positive.
 *
 * Taken from the [Modifier.offsetPx] implementation.
 */
fun Modifier.offset(
    offset: Offset,
) = this.then(OffsetPxModifier(offset.x, offset.y))

/**
 * Offset the content by ([x] px, [y] px). The offsets can be positive as well as non-positive.
 *
 * Taken from the [Modifier.offsetPx] implementation.
 */
fun Modifier.offsetPx(
    x: Float = 0f,
    y: Float = 0f,
) = this.then(OffsetPxModifier(x, y))

private data class OffsetPxModifier(
    val x: Float,
    val y: Float,
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureScope.MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.place(x.roundToInt(), y.roundToInt())
        }
    }
}
