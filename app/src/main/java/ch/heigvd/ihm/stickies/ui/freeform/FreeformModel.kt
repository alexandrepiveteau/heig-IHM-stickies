package ch.heigvd.ihm.stickies.ui.freeform

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.model.Sticky
import ch.heigvd.ihm.stickies.ui.StickiesBlue
import ch.heigvd.ihm.stickies.ui.StickiesOrange
import ch.heigvd.ihm.stickies.ui.StickiesPink
import ch.heigvd.ihm.stickies.ui.StickiesYellow

data class FreeformCategory(
    val title: String,
    @DrawableRes val icon: Int,
    val stickies: List<Sticky>,
)

data class FreeformModel(
    val categories: List<FreeformCategory>,
) {

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
        return FreeformModel(list)
    }
}

// TODO : REMOVE THIS EXAMPLE DATA

val ExamplePileA: List<Sticky> = listOf(
    Sticky(11, Color.StickiesYellow, "Take Medor to the vet", false),
    Sticky(12, Color.StickiesOrange, "Buy some cat food", false)
)

val ExamplePileB: List<Sticky> = listOf(
    Sticky(21, Color.StickiesPink, "Dentist at 10 am", true),
    Sticky(22, Color.StickiesBlue, "Take some Aspirin", false),
    Sticky(23, Color.StickiesYellow, "Call my pharmacist", false)
)

val ExamplePileC: List<Sticky> = listOf(
    Sticky(31, Color.StickiesPink, "Dentist at 10 am", true),
    Sticky(32, Color.StickiesBlue, "Take some Aspirin", false),
    Sticky(33, Color.StickiesYellow, "Call my pharmacist", false)
)

val ExamplePileD: List<Sticky> = listOf(
    Sticky(41, Color.StickiesPink, "Dentist at 10 am", true),
    Sticky(42, Color.StickiesBlue, "Take some Aspirin", false),
    Sticky(43, Color.StickiesYellow, "Call my pharmacist", false)
)

val ExamplePileE: List<Sticky> = listOf(
    Sticky(51, Color.StickiesPink, "Dentist at 10 am", true),
    Sticky(52, Color.StickiesBlue, "Take some Aspirin", false),
    Sticky(53, Color.StickiesYellow, "Call my pharmacist", false)
)

val ExamplePileF: List<Sticky> = listOf(
    Sticky(61, Color.StickiesPink, "Dentist at 10 am", true),
    Sticky(62, Color.StickiesBlue, "Take some Aspirin", false),
    Sticky(63, Color.StickiesYellow, "Call my pharmacist", false)
)

val initialModel = FreeformModel(
    listOf(
        FreeformCategory(
            title = "Inbox",
            icon = R.drawable.ic_category_inbox,
            stickies = ExamplePileA,
        ),
        FreeformCategory(
            title = "Groceries",
            icon = R.drawable.ic_category_inbox,
            stickies = ExamplePileB,
        ),
        FreeformCategory(
            title = "Medical Stuff",
            icon = R.drawable.ic_category_inbox,
            stickies = ExamplePileC,
        ),
        FreeformCategory(
            title = "Appointments",
            icon = R.drawable.ic_category_inbox,
            stickies = ExamplePileD,
        ),
        FreeformCategory(
            title = "Exercise",
            icon = R.drawable.ic_category_inbox,
            stickies = ExamplePileE,
        ),
        FreeformCategory(
            title = "Medor",
            icon = R.drawable.ic_category_inbox,
            stickies = ExamplePileF,
        ),
    )
)