package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.animation.animate
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.ui.StickiesGreen

enum class SelectionDay(val title: String) {
    Monday("Mon"),
    Tuesday("Tue"),
    Wednesday("Wed"),
    Thursday("Thu"),
    Friday("Fri"),
    Saturday("Sat"),
    Sunday("Sun");
}

@Composable
fun DayPicker(
    selected: SelectionDay?,
    onClick: (SelectionDay) -> Unit,
    selectionColor: Color,
    modifier: Modifier = Modifier,
) {
    val defaultColor = Color(0xFFF2F2F2)
    Row(modifier, Arrangement.spacedBy(16.dp)) {
        for (day in SelectionDay.values()) {
            val filled = selected == day
            CircularPill(
                color = animate(if (filled) selectionColor else defaultColor),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        onClick = { onClick(day) },
                        indication = RippleIndication(),
                    ),
                filled = filled,
                content = {
                    Text(day.title, style = MaterialTheme.typography.subtitle1)
                    Text("02", style = MaterialTheme.typography.subtitle2)
                }
            )
        }
    }
}

@Composable
@Preview
private fun DatePickerPreview() {
    Box(Modifier.background(Color.White).padding(16.dp)) {
        val (day, setDay) = remember { mutableStateOf<SelectionDay?>(null) }
        DayPicker(
            selected = day,
            onClick = { date ->
                if (date == day) {
                    setDay(null)
                } else {
                    setDay(date)
                }
            },
            selectionColor = Color.StickiesGreen,
            Modifier.align(Alignment.Center)
        )
    }
}
