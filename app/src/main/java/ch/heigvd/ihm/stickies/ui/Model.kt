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
     * Adds a new sticky to this [Model].
     *
     * @param title the [String] title of the added sticky.
     * @param color the color to be set to the new sticky.
     * @param highlighted true if the sticky is currently highlighted.
     * @param toPile the pile index to which the sticky is added.
     */
    fun stickyAdd(
        title: String,
        color: Color,
        highlighted: Boolean,
        toPile: Int,
    ): Model {
        val sticky = Sticky(
            identifier = StickyIdentifier((this.stickies.maxOfOrNull { it.key.backing } ?: 0) + 1),
            title = title,
            color = color,
            highlighted = highlighted,
            pileIndex = nextHeight,
            category = toPile.coerceIn(0 until categories.size),
        )
        val updated = this.stickies.put(sticky.identifier, sticky)
        return this.copy(
            stickies = updated,
            nextHeight = nextHeight + 1
        )
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

    /**
     * Moves the sticky with the provided [StickyIdentifier] to the pile with the given index,
     * before the given index. If there are no stickies at that index, the sticky will simply be
     * appended to the bottom of the pile.
     *
     * @param identifier the identifier of the moved sticky.
     * @param toIndex the index at which the sticky should be moved. Zero is the pile top.
     */
    fun stickyMoveBeforeIndex(
        identifier: StickyIdentifier,
        toPile: Int,
        toIndex: Int,
    ): Model {
        val sticky = stickies[identifier] ?: error("Missing sticky.")
        var height = nextHeight
        var stickies = this.stickies
        val toShift = this.stickies
            .asSequence()
            .map(Map.Entry<StickyIdentifier, Sticky>::value)
            .filter { it.category == toPile }
            .sortedByDescending(Sticky::pileIndex)
            .filterIndexed { index, _ -> index < toIndex }
            .toList()
        stickies = stickies.put(
            sticky.identifier,
            sticky.copy(category = toPile, pileIndex = height++),
        )
        // We need to reverse the list, to make sure the items we add first are actually not the
        // ones from the start of the pile, but those that came right before our addition.
        for (shiftSticky in toShift.asReversed()) {
            if (shiftSticky.identifier != identifier) {
                stickies = stickies.put(
                    key = shiftSticky.identifier,
                    value = shiftSticky.copy(pileIndex = height++),
                )
            }
        }
        return this.copy(stickies = stickies, nextHeight = height)
    }

    // Used for companion builders.
    companion object
}

