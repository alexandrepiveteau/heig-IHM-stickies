package ch.heigvd.ihm.stickies.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Category(
    val identifier: Long,
    val title: String,
)

// TODO : Switch to domain-specific colors enums.

data class Sticky(
    val identifier: Long,
    val color: Color,
    val title: String,
    val highlighted: Boolean,
)

data class Model(
    val stickies: Map<Category, List<Sticky>>,
)

// Rendering-specific entities.

/**
 * The representation of a renderable sticky.
 *
 * @param identifier the unique identifier of this renderable sticky.
 * @param target the [Offset] that this sticky should be animated to.
 * @param color the surface color of the sticky.
 * @param title the [String] contents of the sticky.
 * @param bubbled whether the bubble should be displayed or not.
 * @param dragged whether the sticky should be elevated and scaled up.
 * @param pileIndex how far in this pile the sticky is. Lowest value is 0.
 * @param pileSize how many items there are in the pile of this sticky. Lowest value is 1.
 */
data class RenderableSticky(
    val identifier: Long,
    val target: Offset,
    val color: Color,
    val title: String,
    val bubbled: Boolean,
    val dragged: Boolean,
    val pileIndex: Int,
    val pileSize: Int,
)