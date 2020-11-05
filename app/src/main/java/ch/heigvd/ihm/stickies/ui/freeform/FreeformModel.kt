package ch.heigvd.ihm.stickies.ui.freeform

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.*
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toPersistentList

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

data class FreeformCategory(
    val title: String,
    @DrawableRes val icon: Int,
)

data class FreeformModel(
    val categories: PersistentList<FreeformCategory>,
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
    fun categoryOpen(index: Int): FreeformModel {
        return copy(open = index)
    }

    /**
     * Closes the currently opened category.
     */
    fun categoryClose(): FreeformModel {
        return copy(open = null)
    }

    /**
     * Swaps two piles with indices [first] and [second]. If the indices are identical (and within
     * the bounds), the model will remain untouched.
     *
     * @param first the first index to swap.
     * @param second the second index to swap.
     *
     * @return the new [FreeformModel].
     */
    fun categorySwap(first: Int, second: Int): FreeformModel {
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
    ): FreeformModel {
        val sticky = (this.stickies[identifier] ?: error("Missing sticky."))
            .copy(category = toPile, pileIndex = nextHeight)
        return this.copy(
            stickies = this.stickies.remove(identifier).put(identifier, sticky),
            nextHeight = nextHeight + 1,
        )
    }
}

// TODO : REMOVE THIS EXAMPLE DATA

private val ExamplePileB: List<Sticky> = listOf(
    Sticky(
        identifier = StickyIdentifier(1),
        category = 1,
        pileIndex = 0,
        color = Color.StickiesPink,
        title = "Buy some bread\n" +
                "\n" +
                "-Whole grain\n" +
                "-Without raisins",
        highlighted = false
    ),
    Sticky(
        identifier = StickyIdentifier(2),
        category = 1,
        pileIndex = 1,
        color = Color.StickiesBlue,
        title = "Buy some bread\n" +
                "\n" +
                "-Whole grain\n" +
                "-Without raisins",
        highlighted = false
    ),
    Sticky(
        identifier = StickyIdentifier(3),
        category = 1,
        pileIndex = 2,
        color = Color.StickiesYellow,
        title = "Buy some bread\n" +
                "\n" +
                "-Whole grain\n" +
                "-Without raisins",
        highlighted = false
    ),
    Sticky(
        identifier = StickyIdentifier(4),
        category = 1,
        pileIndex = 3,
        color = Color.StickiesYellow,
        title = "Buy some bread\n" +
                "\n" +
                "-Whole grain\n" +
                "-Without raisins",
        highlighted = false
    ),
    Sticky(
        identifier = StickyIdentifier(5),
        category = 1,
        pileIndex = 3,
        color = Color.StickiesGreen,
        title = "Buy some bread\n" +
                "\n" +
                "-Whole grain\n" +
                "-Without raisins",
        highlighted = false
    ),
    Sticky(
        identifier = StickyIdentifier(6),
        category = 1,
        pileIndex = 3,
        color = Color.StickiesOrange,
        title = "Buy some bread\n" +
                "\n" +
                "-Whole grain\n" +
                "-Without raisins",
        highlighted = false
    ),
    Sticky(
        identifier = StickyIdentifier(7),
        category = 1,
        pileIndex = 3,
        color = Color.StickiesBlue,
        title = "Buy some bread\n" +
                "\n" +
                "-Whole grain\n" +
                "-Without raisins",
        highlighted = false
    )
)

private val categories = listOf(
    FreeformCategory("Inbox", R.drawable.ic_category_inbox),
    FreeformCategory("Groceries", R.drawable.ic_category_basket),
    FreeformCategory("Medical Stuff", R.drawable.ic_category_medical),
    FreeformCategory("Appointments", R.drawable.ic_category_basket),
    FreeformCategory("Exercise", R.drawable.ic_category_exercise),
    FreeformCategory("Medor", R.drawable.ic_category_bone),
)

private fun build(): PersistentMap<StickyIdentifier, Sticky> {
    var items = persistentHashMapOf<StickyIdentifier, Sticky>()
    for (sticky in ExamplePileB) {
        items = items.put(sticky.identifier, sticky)
    }
    return items
}

val initialModel = FreeformModel(
    categories = categories.toPersistentList(),
    stickies = build(),
    open = null,
)