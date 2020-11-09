package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun StickyDetails(
    modifier: Modifier = Modifier,
) {
    val (dates, setDates) = remember { mutableStateOf(emptySet<SelectionDate>()) }
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val (color, setColor) = remember { mutableStateOf(SelectionColor.Pink) }

    Column(modifier) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                )
                .align(Alignment.CenterHorizontally),

        ) {
            Column(
                Modifier
                    .padding(32.dp)
                    .width(90.dp.times(7))
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
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                )

                ExpendableButton(
                    expanded = expanded,
                    onClick = setExpanded,
                    color = Color(0x999999),
                    contractedText = "More settings",
                    expandedText = "Less settings",
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally),
                )

                if (expanded) {
                    TimePicker(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                )
                .align(Alignment.CenterHorizontally),
        ) {
            ColorPicker(
                selected = color,
                onClick = setColor,
                modifier = Modifier.padding(32.dp),
            )
        }
    }
}

@Composable
@Preview
private fun StickyDetailsPreview() {
    Column() {
        StickyDetails(
            modifier = Modifier.padding(0.dp),
        )

        Spacer(
            Modifier.height(16.dp),
        )

        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.White),
        ) {
            Text("Back to Home.")
        }
    }
}