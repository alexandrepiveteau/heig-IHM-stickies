package ch.heigvd.ihm.stickies.ui

import androidx.compose.ui.graphics.Color
import ch.heigvd.ihm.stickies.R
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toPersistentList

private val DemoCategories = listOf(
    Category("Inbox", R.drawable.ic_category_inbox),
    Category("Groceries", R.drawable.ic_category_basket),
    Category("Medical Stuff", R.drawable.ic_category_medical),
    Category("Appointments", R.drawable.ic_category_calendar),
    Category("Exercise", R.drawable.ic_category_exercise),
    Category("Medor", R.drawable.ic_category_bone),
)

/**
 * Returns a new [Model] with some default data, which can be used as a basis when the application
 * starts.
 */
// TODO : Actually provide some default data.
fun Model.Companion.default(): Model = Model(
    categories = DemoCategories.toPersistentList(),
    stickies = persistentHashMapOf(),
    open = null,
)

/**
 * Returns a new [Model] with some fake data, which can be used for demonstrations of the different
 * features. Unlike the [default] data, which can act as a tutorial, the [demo] data contains some
 * real-world stuff.
 */
fun Model.Companion.demo(): Model = Model(
    categories = DemoCategories.toPersistentList(),
    stickies = persistentHashMapOf(),
    open = null,
)
    // Inbox.
    // Groceries.
    .stickyAdd("A new tablet", Color.StickiesYellow, null, 1)
    .stickyAdd("Neew keyybo rd", Color.StickiesGreen, null, 1)
    .stickyAdd("Buy potato", Color.StickiesOrange, null, 1)
    .stickyAdd("Birthday present for Andres", Color.StickiesPink, null, 1)
    .stickyAdd("6 hamburgers\n- Buns\n- Steaks\n- Tomatoes", Color.StickiesBlue, null, 1)
    .stickyAdd("Buy some bread\n- Whole grain\n- Without raisins", Color.StickiesYellow, System.currentTimeMillis() + 30 * 1000, 1)
    // Medical stuff.
    .stickyAdd("Take all my pills for the day :\n- Aspirin\n- Lotensin",
        Color.StickiesOrange,
        null,
        2)
    // Appointments.
    .stickyAdd("Netflix & Chill with Jeanette", Color.StickiesYellow, null, 3)
    .stickyAdd("IHM presentation", Color.StickiesBlue, null, 3)
    .stickyAdd("Dentist at 10 am", Color.StickiesOrange, System.currentTimeMillis() + 60 * 1000, 3)
    // Exercice.
    .stickyAdd("Walk around the block\n(use a face mask)", Color.StickiesGreen, null, 4)
    // Medor.
    .stickyAdd("Tell him he's a good boy", Color.StickiesOrange, null, 5)
    .stickyAdd("Buy a new bone", Color.StickiesBlue, null, 5)
    .stickyAdd("Take Medor to the vet", Color.StickiesYellow, System.currentTimeMillis() + 240 * 1000, 5)
