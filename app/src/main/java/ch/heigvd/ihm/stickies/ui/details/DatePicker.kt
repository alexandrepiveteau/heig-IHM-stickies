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
import java.util.*

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
        selected: EnumMap<SelectionDate, Boolean>,
        onClick: (EnumMap<SelectionDate, Boolean>) -> EnumMap<SelectionDate, Boolean>,
        modifier: Modifier = Modifier,
) {
    Row(modifier) {
        CircularPill(
            color = Color(0xFFF2F2F2),
            Modifier.clickable(onClick = { onClick( { oldEnumMap -> oldEnumMap[SelectionDate.Monday] = !oldEnumMap[SelectionDate.Monday] }) }, indication = null),
            filled = selected[SelectionDate.Monday] ?: false,
            content = {
                Text("Mon", style = MaterialTheme.typography.subtitle1)
                Text("02", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick( { oldEnumMap -> oldEnumMap[SelectionDate.Tuesday] = !oldEnumMap[SelectionDate.Tuesday] }) }, indication = null),
                filled = selected[SelectionDate.Tuesday] ?: false,
                content = {
                Text("Tue", style = MaterialTheme.typography.subtitle1)
                Text("03", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick( { oldEnumMap -> oldEnumMap[SelectionDate.Wednesday] = !oldEnumMap[SelectionDate.Wednesday] }) }, indication = null),
                filled = selected[SelectionDate.Wednesday] ?: false,
                content = {
                Text("Wed", style = MaterialTheme.typography.subtitle1)
                Text("04", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick( { oldEnumMap -> oldEnumMap[SelectionDate.Thursday] = !oldEnumMap[SelectionDate.Thursday] }) }, indication = null),
                filled = selected[SelectionDate.Thursday] ?: false,
                content = {
                Text("Thu", style = MaterialTheme.typography.subtitle1)
                Text("05", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick( { oldEnumMap -> oldEnumMap[SelectionDate.Friday] = !oldEnumMap[SelectionDate.Friday] }) }, indication = null),
                filled = selected[SelectionDate.Friday] ?: false,
                content = {
                Text("Fri", style = MaterialTheme.typography.subtitle1)
                Text("06", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick( { oldEnumMap -> oldEnumMap[SelectionDate.Saturday] = !oldEnumMap[SelectionDate.Saturday] }) }, indication = null),
                filled = selected[SelectionDate.Saturday] ?: false,
                content = {
                Text("Sat", style = MaterialTheme.typography.subtitle1)
                Text("07", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
        CircularPill(
                color = Color(0xFFF2F2F2),
                Modifier.clickable(onClick = { onClick( { oldEnumMap -> oldEnumMap[SelectionDate.Sunday] = !oldEnumMap[SelectionDate.Sunday] }) }, indication = null),
                filled = selected[SelectionDate.Sunday] ?: false,
                content = {
                Text("Sun", style = MaterialTheme.typography.subtitle1)
                Text(".", style = MaterialTheme.typography.subtitle2)
            })
        Spacer(Modifier.width(16.dp))
    }
}

@Composable
private fun DatePickerPreview() {
    Stack(Modifier.background(Color.White).padding(16.dp)) {
        val (dates, setDates) = remember {
            mutableStateOf(
                    SelectionDate.values()
                            .zip(MutableList(7) { false })
                            .toMap(EnumMap(SelectionDate::class.java))
            )
        }
        DatePicker(selected = dates, onClick = {  setDates -> setDates }, Modifier.align(Alignment.Center))
    }
}