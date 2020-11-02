package ch.heigvd.ihm.stickies.ui.freeform

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.model.Sticky
import ch.heigvd.ihm.stickies.ui.*

data class FreeformCategory(
    val title: String,
    @DrawableRes val icon: Int,
    val stickies: List<Sticky>,
)

data class FreeformModel(
    val categories: List<FreeformCategory>,
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
    fun swap(first: Int, second: Int): FreeformModel {
        val a = categories[first].stickies
        val b = categories[second].stickies
        val catA = categories[first].copy(stickies = b)
        val catB = categories[second].copy(stickies = a)
        val list = categories.toMutableList().apply {
            set(first, catA)
            set(second, catB)
        }
        return copy(categories = list)
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

val initialModel = FreeformModel(
    categories = listOf(
        FreeformCategory(
            title = "Inbox",
            icon = R.drawable.ic_category_inbox,
            stickies = ExamplePileA,
        ),
        FreeformCategory(
            title = "Groceries",
            icon = R.drawable.ic_category_basket,
            stickies = ExamplePileB,
        ),
        FreeformCategory(
            title = "Medical Stuff",
            icon = R.drawable.ic_category_medical,
            stickies = ExamplePileC,
        ),
        FreeformCategory(
            title = "Appointments",
            icon = R.drawable.ic_category_basket,
            stickies = ExamplePileD,
        ),
        FreeformCategory(
            title = "Exercise",
            icon = R.drawable.ic_category_exercise,
            stickies = ExamplePileE,
        ),
        FreeformCategory(
            title = "Medor",
            icon = R.drawable.ic_category_bone,
            stickies = ExamplePileF,
        ),
    ),
    open = null,
)