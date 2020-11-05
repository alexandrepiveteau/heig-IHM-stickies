package ch.heigvd.ihm.stickies.ui.details

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

enum class SelectionDate(val title: String) {
    Monday("Mon"),
    Tuesday("Tue"),
    Wednesday("Wed"),
    Thursday("Thu"),
    Friday("Fri"),
    Saturday("Sat"),
    Sunday("Sun");
}

@Composable
fun DatePicker(
    selected: Set<SelectionDate>,
    onClick: (SelectionDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        for (day in SelectionDate.values()) {
            CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier
                    .clip(CircleShape)
                    .clickable(
                        onClick = { onClick(day) },
                        indication = RippleIndication(),
                    ),
                filled = selected.contains(day),
                content = {
                    Text(day.title, style = MaterialTheme.typography.subtitle1)
                    Text("02", style = MaterialTheme.typography.subtitle2)
                })
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
@Preview
private fun DatePickerPreview() {
    Stack(Modifier.background(Color.White).padding(16.dp)) {
        val (dates, setDates) = remember { mutableStateOf(emptySet<SelectionDate>()) }
        DatePicker(
            selected = dates,
            onClick = { date ->
                if (dates.contains(date)) {
                    setDates(dates - date)
                } else {
                    setDates(dates + date)
                }
            },
            Modifier.align(Alignment.Center)
        )
    }
}