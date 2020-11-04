package ch.heigvd.ihm.stickies.ui.freeform

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.DensityAmbient
import ch.heigvd.ihm.stickies.ui.stickies.StickySize

/**
 * An interface describing the scope in which freeform components are being placed. This scope will
 * be provided to components that are displayed in a [Freeform] composable.
 */
interface FreeformScope {
    val origin: Offset
    val size: Offset
    val cellSize: Offset
    val spacer: Offset
}

/**
 * A [Freeform] composable allows for positioning children with information provided by
 * a [FreeformScope]. This scope contains information relative to a pane dimensions, its origin,
 * as well as the size of cells that are displayed on top of it.
 *
 * Additionally, it calculates some spacer values for items of the grid.
 *
 * TODO : Pass the grid parameters as arguments.
 * TODO : Support dynamic grid sizes, since the item sizes are passed to the children anyways.
 *
 * @param modifier the [Modifier] to apply to children.
 * @param content the children that will be displayed.
 */
@Composable
fun Freeform(
    modifier: Modifier = Modifier,
    content: @Composable FreeformScope.() -> Unit,
) {
    WithConstraints {
        val width = with(DensityAmbient.current) { maxWidth.toPx() }
        val height = with(DensityAmbient.current) { maxHeight.toPx() }
        val size = Offset(x = width, y = height)
        val stickySize = with(DensityAmbient.current) { StickySize.toPx() }
        val stickySizeOffset = Offset(stickySize, stickySize)

        // Paddings to be respected between the grid elements.
        val spacerX =
            (width - (FreeformConstants.GridHorizontalCellCount * stickySize)) /
                    (FreeformConstants.GridHorizontalCellCount + 1)
        val spacerY =
            (height - (FreeformConstants.GridVerticalCellCount * stickySize)) /
                    (FreeformConstants.GridVerticalCellCount + 1)
        val spacer = Offset(x = spacerX, y = spacerY)

        // Create a scope with the provided data.
        val scope = FreeformScopeImpl(
            origin = Offset.Zero,
            size = size,
            cellSize = stickySizeOffset,
            spacer = spacer,
        )

        // Invoke the scope.
        Box(modifier) {
            content.invoke(scope)
        }
    }
}

object FreeformConstants {

    // Grid dimensions.
    const val GridHorizontalCellCount = 3
    const val GridVerticalCellCount = 2
}

private data class FreeformScopeImpl(
    override val origin: Offset,
    override val size: Offset,
    override val cellSize: Offset,
    override val spacer: Offset
) : FreeformScope
