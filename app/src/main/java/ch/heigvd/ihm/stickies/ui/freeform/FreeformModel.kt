package ch.heigvd.ihm.stickies.ui.freeform

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.model.Sticky
import ch.heigvd.ihm.stickies.ui.*
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

data class FreeformSticky(
    val categoryIndex: Int,
    val sticky: Sticky,
    val dragState: DragState,
)

data class FreeformCategory(
    val title: String,
    @DrawableRes val icon: Int,
)

data class FreeformModel(
    val categories: PersistentList<FreeformCategory>,
    val stickies: PersistentList<FreeformSticky>,
    val open: Int?,
) {

    /**
     * Returns true if a certain category is currently open.
     */
    val isOpen: Boolean
        get() = open != null

    /**
     * Swaps two piles with indices [first] and [second]. If the indices are identical (and within
     * the bounds), the model will remain untouched.
     *
     * @param first the first index to swap.
     * @param second the second index to swap.
     *
     * @return the new [FreeformModel].
     */
    fun swapCategories(first: Int, second: Int): FreeformModel {
        val stickies = this.stickies.asSequence()
            .map {
                when (it.categoryIndex) {
                    first -> it.copy(categoryIndex = second)
                    second -> it.copy(categoryIndex = first)
                    else -> it
                }
            }
            .asIterable()
            .toPersistentList()

        val catA = categories[first]
        val catB = categories[second]

        val categories = this.categories
            .set(first, catB)
            .set(second, catA)

        return copy(categories = categories, stickies = stickies)
    }

    fun move(identifier: Long, toPile: Int): FreeformModel {
        val index = this.stickies.indexOfFirst { it.sticky.identifier == identifier }
        val sticky = this.stickies[index].copy(categoryIndex = toPile)
        return this.copy(
            stickies = stickies.removeAt(index).add(0, sticky)
        )
    }

    fun updateStickyDrag(identifier: Long, state: DragState): FreeformModel {
        val index = this.stickies.indexOfFirst { it.sticky.identifier == identifier }
        return this.copy(
            stickies = stickies.set(
                index,
                stickies[index].copy(
                    dragState = state,
                ),
            )
        )
    }
}

// TODO : REMOVE THIS EXAMPLE DATA

val ExamplePileA: List<Sticky> = emptyList()

val ExamplePileB: List<Sticky> = listOf(
    Sticky(21, Color.StickiesYellow, "Buy some bread\n\n-Whole grain\n-Without raisins", true),
    Sticky(22, Color.StickiesBlue, "", false),
    Sticky(23, Color.StickiesPink, "", false),
    Sticky(24, Color.StickiesOrange, "", false)
)

val ExamplePileC: List<Sticky> = listOf(
    Sticky(
        31,
        Color.StickiesOrange,
        "Take all my pills for the day :\n\n-Aspirin\n-Lotensin",
        true
    ),
)

val ExamplePileD: List<Sticky> = listOf(
    Sticky(41, Color.StickiesOrange, "Dentist at 10 am", true),
    Sticky(42, Color.StickiesBlue, "", false),
    Sticky(43, Color.StickiesYellow, "", false),
)

val ExamplePileE: List<Sticky> = listOf(
    Sticky(51, Color.StickiesGreen, "Walk around the block\n(use a face mask)", false),
)

val ExamplePileF: List<Sticky> = listOf(
    Sticky(61, Color.StickiesYellow, "Take Medor to the vet", true),
    Sticky(62, Color.StickiesBlue, "", false),
    Sticky(63, Color.StickiesPink, "", false)
)

private fun List<Sticky>.toFreeform(category: Int): List<FreeformSticky> {
    return map { FreeformSticky(category, it, NotDragging()) }
}

val categories = listOf(
    FreeformCategory("Inbox", R.drawable.ic_category_inbox),
    FreeformCategory("Groceries", R.drawable.ic_category_basket),
    FreeformCategory("Medical Stuff", R.drawable.ic_category_medical),
    FreeformCategory("Appointments", R.drawable.ic_category_basket),
    FreeformCategory("Exercise", R.drawable.ic_category_exercise),
    FreeformCategory("Medor", R.drawable.ic_category_bone),
)

val initialModel = FreeformModel(
    categories = categories.toPersistentList(),
    stickies = listOf(
        ExamplePileA.toFreeform(0),
        ExamplePileB.toFreeform(1),
        ExamplePileC.toFreeform(2),
        ExamplePileD.toFreeform(3),
        ExamplePileE.toFreeform(4),
        ExamplePileF.toFreeform(5),
    ).flatten().toPersistentList(),
    open = null,
)