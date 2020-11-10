package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StickyDetails(
    color: Color,
    text: String,
    onColorChange: (Color) -> Unit,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {
    val (dates, setDates) = remember { mutableStateOf(emptySet<SelectionDate>()) }
    val (expanded, setExpanded) = remember { mutableStateOf(false) }

    Row(
        modifier,
        Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        Alignment.CenterVertically,
    ) {
        Column(Modifier, Arrangement.spacedBy(16.dp)) {
            Portion(title = "ADD A REMINDER") {
                DatePicker(
                    selected = dates,
                    onClick = { date ->
                        if (dates.contains(date)) {
                            setDates(dates - date)
                        } else {
                            setDates(dates + date)
                        }
                    },
                )
                AnimatedVisibility(
                    expanded,
                    enter = fadeIn() + expandVertically(Alignment.CenterVertically),
                    exit = fadeOut() + shrinkVertically(Alignment.CenterVertically),
                ) {
                    Spacer(Modifier.height(16.dp))
                    TimePicker()
                }
                Spacer(Modifier.height(16.dp))
                ExpandButton(
                    expanded = expanded,
                    onClick = { setExpanded(!expanded) },
                )
            }
            // This size is known to be 76 * 7 + 16 * 8, aka the width of the CircularPill composable
            // plus the spacers of the Portion composable.
            Portion("CHOOSE A COLOR", Modifier.preferredWidth(660.dp)) {
                ColorPicker(selected = color, onClick = onColorChange)
            }
            Row(
                Modifier.preferredWidth(660.dp),
                Arrangement.spacedBy(64.dp, Alignment.CenterHorizontally),
            ) {
                actions()
            }
        }
        EditSticky(
            text = text,
            onTextChange = onTextChange,
            color = color,
            Modifier
                .padding(bottom = 64.dp + 16.dp)
                .size(376.dp)
        )
    }
}
