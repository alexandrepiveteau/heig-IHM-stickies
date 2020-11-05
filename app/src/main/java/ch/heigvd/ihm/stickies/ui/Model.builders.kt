package ch.heigvd.ihm.stickies.ui

import androidx.compose.ui.graphics.Color
import ch.heigvd.ihm.stickies.R
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toPersistentList

/**
 * Returns a new [Model] with some default data, which can be used as a basis when the application
 * starts.
 */
// TODO : Actually provide some default data.
fun Model.Companion.default() = Model(
    categories = categories.toPersistentList(),
    stickies = build(),
    open = null,
)

/**
 * Returns a new [Model] with some fake data, which can be used for demonstrations of the different
 * features. Unlike the [default] data, which can act as a tutorial, the [demo] data contains some
 * real-world stuff.
 */
// TODO : Actually provide some demo data.
fun Model.Companion.demo(): Model {
    return default()
}

// DEFAULT EXAMPLE DATA.

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
    Category("Inbox", R.drawable.ic_category_inbox),
    Category("Groceries", R.drawable.ic_category_basket),
    Category("Medical Stuff", R.drawable.ic_category_medical),
    Category("Appointments", R.drawable.ic_category_basket),
    Category("Exercise", R.drawable.ic_category_exercise),
    Category("Medor", R.drawable.ic_category_bone),
)

private fun build(): PersistentMap<StickyIdentifier, Sticky> {
    var items = persistentHashMapOf<StickyIdentifier, Sticky>()
    for (sticky in ExamplePileB) {
        items = items.put(sticky.identifier, sticky)
    }
    return items
}