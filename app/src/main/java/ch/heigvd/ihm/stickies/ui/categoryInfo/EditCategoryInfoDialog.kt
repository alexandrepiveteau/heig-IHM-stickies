package ch.heigvd.ihm.stickies.ui.categoryInfo

import androidx.annotation.DrawableRes
import androidx.compose.animation.animate
import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.R
import ch.heigvd.ihm.stickies.ui.StickiesSuperGraddyStart

// All the icons that are available to end users and that can be set on categories.
private val AvailableIcons = listOf(
    R.drawable.ic_category_basket,
    R.drawable.ic_category_bone,
    R.drawable.ic_category_calendar,
    R.drawable.ic_category_exercise,
    R.drawable.ic_category_inbox,
    R.drawable.ic_category_medical,
)
private const val GridHorizontalCellCount = 8

/**
 * A [Dialog] that will be used to let the user edit the currently set category information. It
 * allows for cancellation of edition, if necessary / appropriate.
 *
 * @param title the previously set [title] of the category.
 * @param icon the previously set [icon] of the category.
 * @param onConfirm called when some new title and icon pair is set for the category.
 * @param onCancel called when the dialog is dismissed and changes are discarded.
 */
@OptIn(ExperimentalLayout::class)
@Composable
fun EditCategoryInfoDialog(
    title: String,
    @DrawableRes icon: Int,
    onConfirm: (title: String, icon: Int) -> Unit,
    onCancel: () -> Unit,
) {
    MaterialTheme(
        colors = lightColors(primary = Color.StickiesSuperGraddyStart)
    ) {
        Dialog(onDismissRequest = onCancel) {
            val (newText, setNewText) = remember { mutableStateOf(title) }
            val (newIcon, setNewIcon) = remember { mutableStateOf(icon) }

            Surface(Modifier.preferredWidth(400.dp), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Edit category", style = MaterialTheme.typography.h6)
                    Text("Name",
                        Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.caption)
                    OutlinedTextField(newText, setNewText, Modifier.fillMaxWidth())
                    Text("Icon",
                        Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.caption)
                    IconPicker(
                        selected = newIcon,
                        onSelected = setNewIcon,
                        modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth()
                    )
                    Row(Modifier.align(Alignment.End).padding(top = 16.dp)) {
                        Button(
                            onClick = onCancel,
                            colors = ButtonConstants.defaultTextButtonColors(),
                            elevation = null,
                        ) { Text("Cancel") }
                        Button(
                            onClick = { onConfirm(newText, newIcon) },
                            colors = ButtonConstants.defaultButtonColors(),
                            modifier = Modifier.padding(start = 16.dp)
                        ) { Text("Save") }
                    }
                }
            }
        }
    }
}

@Composable
private fun IconPicker(
    @DrawableRes selected: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumnFor(AvailableIcons.chunked(GridHorizontalCellCount), modifier) { items ->
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            for (item in items) {
                val color =
                    if (selected == item) Color.StickiesSuperGraddyStart
                    else Color.Black.copy(alpha = 0.2f)
                Providers(AmbientContentColor provides animate(color)) {
                    IconButton(onClick = { onSelected(item) }) {
                        Icon(vectorResource(item))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditCategoryInfoDialogPreview() {
    MaterialTheme {
        EditCategoryInfoDialog(
            title = "Groceries",
            icon = R.drawable.ic_category_basket,
            onConfirm = { _, _ -> },
            onCancel = {},
        )
    }
}