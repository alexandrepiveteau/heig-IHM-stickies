package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
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
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (dates, setDates) = remember { mutableStateOf(emptySet<SelectionDate>()) }
    val (expanded, setExpanded) = remember { mutableStateOf(false) }

    Column(modifier) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .padding(
                    horizontal = 32.dp,
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

                ExpandButton(
                    expanded = expanded,
                    onClick = { setExpanded(!expanded) },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterHorizontally),
                )

                AnimatedVisibility(expanded) {
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
                    horizontal = 32.dp,
                    vertical = 8.dp,
                )
                .align(Alignment.CenterHorizontally),
        ) {
            ColorPicker(
                selected = color,
                onClick = onColorChange,
                modifier = Modifier.padding(32.dp),
            )
        }
    }
}