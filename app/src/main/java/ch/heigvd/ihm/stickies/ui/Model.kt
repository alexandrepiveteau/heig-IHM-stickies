package ch.heigvd.ihm.stickies.ui

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf

inline class StickyIdentifier(
    val backing: Long,
)

data class Sticky(
    // Unique identification.
    val identifier: StickyIdentifier,

    // Position information.
    val category: Int,
    val pileIndex: Long,

    // Sticky properties.
    val color: Color,
    val title: String,
    val highlighted: Boolean,
)

data class Category(
    val title: String,
    @DrawableRes val icon: Int,
)

data class Model(
    val categories: PersistentList<Category>,
    val stickies: PersistentMap<StickyIdentifier, Sticky>,
    private val nextHeight: Long = stickies.size.toLong() + 1,
    private val open: Int?,
) {

    /**
     * Returns true if a certain category is currently open.
     */
    val categoryOpen: Boolean
        get() = open != null

    /**
     * Returns the index of the currently opened category.
     */
    val categoryOpenIndex: Int?
        get() = open

    /**
     * Opens an existing category for the provided index. If another category was previously
     * opened, the current category will be closed.
     *
     * @param index the category to open.
     */
    fun categoryOpen(index: Int): Model {
        return copy(open = index)
    }

    /**
     * Closes the currently opened category.
     */
    fun categoryClose(): Model {
        return copy(open = null)
    }

    /**
     * Updates the title of a category. If the index of the said category is not valid or is null,
     * the current [Model] will remain untouched.
     *
     * @param index the nullable index at which to update the model.
     * @param title the new title to set to the category.
     *
     * @return the new [Model].
     */
    fun categoryUpdateTitle(index: Int?, title: String): Model {
        if (index == null || index >= categories.size) return this
        val updated = categories[index].copy(title = title)
        val list = categories.set(index, updated)
        return this.copy(categories = list)
    }

    /**
     * Swaps two piles with indices [first] and [second]. If the indices are identical (and within
     * the bounds), the model will remain untouched.
     *
     * @param first the first index to swap.
     * @param second the second index to swap.
     *
     * @return the new [Model].
     */
    fun categorySwap(first: Int, second: Int): Model {
        var stickies = persistentHashMapOf<StickyIdentifier, Sticky>()
        for ((key, value) in this.stickies) {
            stickies = when (value.category) {
                first -> stickies.put(key, value.copy(category = second))
                second -> stickies.put(key, value.copy(category = first))
                else -> stickies.put(key, value)
            }
        }

        val catA = categories[first]
        val catB = categories[second]

        val categories = this.categories
            .set(first, catB)
            .set(second, catA)

        return copy(categories = categories, stickies = stickies)
    }

    /**
     * Moves the sticky with the provided [StickyIdentifier] to the pile with the given index.
     *
     * @param toPile the pile to which the sticky is moved.
     */
    fun stickyMoveToTop(
        identifier: StickyIdentifier,
        toPile: Int,
    ): Model {
        val sticky = (this.stickies[identifier] ?: error("Missing sticky."))
            .copy(category = toPile, pileIndex = nextHeight)
        return this.copy(
            stickies = this.stickies.remove(identifier).put(identifier, sticky),
            nextHeight = nextHeight + 1,
        )
    }

    // Used for companion builders.
    companion object
}

