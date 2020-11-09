package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import java.time.LocalTime

@Composable
fun StickyDetails(
    modifier: Modifier = Modifier,
) {
    val (dates, setDates) = remember { mutableStateOf(emptySet<SelectionDate>()) }
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val (color, setColor) = remember { mutableStateOf(SelectionColor.Pink) }

    Column(modifier
        .padding(32.dp)
    ) {
        DatePicker(
            selected = dates,
            onClick = { date ->
                if (dates.contains(date)) {
                    setDates(dates - date)
                } else {
                    setDates(dates + date)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        ExpendableButton(
            expanded = expanded,
            onClick = setExpanded,
            color = Color(0x999999),
            text = "More settings",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        if (expanded) {
            TimePicker(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }

        Spacer(
            modifier = Modifier
                .background(Color.Black)
                .height(16.dp),
        )

        ColorPicker(
            selected = color,
            onClick = setColor,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
@Preview
private fun StickyDetailsPreview() {
    Box(
        Modifier
            .padding(64.dp)
            .background(Color.White)
            .fillMaxSize()
    ) {
        StickyDetails()
    }
}