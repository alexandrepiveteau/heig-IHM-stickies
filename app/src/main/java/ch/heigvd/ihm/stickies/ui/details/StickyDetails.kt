package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.animation.*
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.Instant
import java.time.Year
import java.time.ZoneId

private fun DayOfWeek.asSelectionDay(): SelectionDay = when (this) {
    DayOfWeek.MONDAY -> SelectionDay.Monday
    DayOfWeek.TUESDAY -> SelectionDay.Tuesday
    DayOfWeek.WEDNESDAY -> SelectionDay.Wednesday
    DayOfWeek.THURSDAY -> SelectionDay.Thursday
    DayOfWeek.FRIDAY -> SelectionDay.Friday
    DayOfWeek.SATURDAY -> SelectionDay.Saturday
    DayOfWeek.SUNDAY -> SelectionDay.Sunday
}

private fun hour(timestamp: Long): Int {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .hour
}

private fun minute(timestamp: Long): Int {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .minute
}

private fun day(timestamp: Long): SelectionDay {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .dayOfWeek
        .asSelectionDay()
}

private fun dayOfMonth(timestamp: Long): Int {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .dayOfMonth
}

private fun daysInMonth(timestamp: Long): Int {
    val dateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
    return dateTime
        .month
        .length(Year.isLeap(dateTime.year.toLong()))
}

private fun asTimestamp(
    today: SelectionDay,
    hour: Int,
    minute: Int,
    selectionDay: SelectionDay?,
) =
    selectionDay?.let { day ->
        val now = Instant.ofEpochMilli(System.currentTimeMillis())
            .atZone(ZoneId.systemDefault())

        return@let now.plusDays((day - today).toLong())
            .withHour(hour)
            .withMinute(minute)
            .toInstant()
            .toEpochMilli()
    }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StickyDetails(
    color: Color,
    text: String,
    alert: Long?,
    onColorChange: (Color) -> Unit,
    onTextChange: (String) -> Unit,
    onAlertChange: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {
    val now = remember { System.currentTimeMillis() }
    val (hour, setHour) = remember { mutableStateOf(alert?.let(::hour) ?: 9) }
    val (minute, setMinute) = remember { mutableStateOf(alert?.let(::minute) ?: 0) }
    val (day, setDay) = remember { mutableStateOf(alert?.let(::day)) }
    val (expanded, setExpanded) = remember { mutableStateOf(false) }

    Row(
        modifier,
        Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        Alignment.CenterVertically,
    ) {
        Column(Modifier, Arrangement.spacedBy(16.dp)) {
            Portion(title = "ADD A REMINDER") {
                DayPicker(
                    selected = day,
                    onClick = { date ->
                        onAlertChange(asTimestamp(day(now), hour, minute, day))
                        if (day == date) {
                            // Callback.
                            setDay(null)
                        } else {
                            // Callback.
                            setDay(date)
                        }
                    },
                    selectionColor = color,
                    today = day(now),
                    dayOfMonth = dayOfMonth(now),
                    daysInMonths = daysInMonth(now),
                )
                AnimatedVisibility(
                    expanded,
                    enter = fadeIn() + expandVertically(Alignment.CenterVertically),
                    exit = fadeOut() + shrinkVertically(Alignment.CenterVertically),
                ) {
                    Spacer(Modifier.height(16.dp))
                    TimePicker(
                        initialHour = hour,
                        initialMinute = minute,
                        onHour = {
                            onAlertChange(asTimestamp(day(now), hour, minute, day))
                            setHour(it)
                        },
                        onMinute = {
                            onAlertChange(asTimestamp(day(now), hour, minute, day))
                            setMinute(it)
                        },
                    )
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
