package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

enum class SelectionDate {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday,
}

@Composable
fun DatePicker(
    selected: SelectionDate,
    onClick: (SelectionDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        CircularPill(
            color = Color(0xFFF2F2F2),
            Modifier.clickable(onClick = { onClick(SelectionDate.Monday) }, indication = null),
            filled = selected == SelectionDate.Monday,
            content = {
                Text("Mon", style = MaterialTheme.typography.subtitle1)
                Text("02", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick(SelectionDate.Tuesday) }, indication = null),
                filled = selected == SelectionDate.Tuesday,
                content = {
                Text("Tue", style = MaterialTheme.typography.subtitle1)
                Text("03", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick(SelectionDate.Wednesday) }, indication = null),
                filled = selected == SelectionDate.Wednesday,
                content = {
                Text("Wed", style = MaterialTheme.typography.subtitle1)
                Text("04", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick(SelectionDate.Thursday) }, indication = null),
                filled = selected == SelectionDate.Thursday,
                content = {
                Text("Thu", style = MaterialTheme.typography.subtitle1)
                Text("05", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick(SelectionDate.Friday) }, indication = null),
                filled = selected == SelectionDate.Friday,
                content = {
                Text("Fri", style = MaterialTheme.typography.subtitle1)
                Text("06", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick(SelectionDate.Saturday) }, indication = null),
                filled = selected == SelectionDate.Saturday,
                content = {
                Text("Sat", style = MaterialTheme.typography.subtitle1)
                Text("07", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick(SelectionDate.Sunday) }, indication = null),
                filled = selected == SelectionDate.Sunday,
                content = {
                Text("Sun", style = MaterialTheme.typography.subtitle1)
                Text(".", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
    }
}

@Composable
@Preview
private fun DatePickerPreview() {
    Stack(Modifier.background(Color.White).padding(16.dp)) {
        val (date, setDate) = remember { mutableStateOf(SelectionDate.Sunday) }
        DatePicker(selected = date, onClick = setDate, Modifier.align(Alignment.Center))
    }
}