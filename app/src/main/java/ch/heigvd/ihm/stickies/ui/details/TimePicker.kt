package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import ch.heigvd.ihm.stickies.ui.Archivo
import ch.heigvd.ihm.stickies.ui.modifier.drawVerticalOverlay

private val TimeTextLineHeight = 42.sp
private val TimeTextHeight = 30.sp

private val TimeTextStyle = TextStyle(
    fontFamily = Archivo,
    fontWeight = FontWeight.Bold,
    fontSize = TimeTextHeight,
    lineHeight = TimeTextLineHeight,
)

@Composable
fun TimePicker(
    initialHour: Int,
    initialMinute: Int,
    onHour: (Int) -> Unit,
    onMinute: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val hours = (0..23).distinct()
        val hoursState = rememberLazyListState(initialHour)

        val minutes = (0..59).distinct()
        val minutesState = rememberLazyListState(initialMinute)

        val itemHeight = with(DensityAmbient.current) { TimeTextLineHeight.toIntPx() }

        val currentHour =
            if (hoursState.firstVisibleItemScrollOffset > itemHeight / 2) hoursState.firstVisibleItemIndex + 1
            else hoursState.firstVisibleItemIndex

        val currentMinute =
            if (minutesState.firstVisibleItemScrollOffset > itemHeight / 2) minutesState.firstVisibleItemIndex + 1
            else minutesState.firstVisibleItemIndex

        // A bit ugly, but necessary to extract our state.
        remember(currentHour) { onHour(currentHour) }
        remember(currentMinute) { onMinute(currentMinute) }

        Clock(
            hours = hours[currentHour],
            minutes = minutes[currentMinute],
            Modifier.size(156.dp)
        )

        Spacer(Modifier.width(64.dp))

        NumberPicker(
            current = currentHour,
            numbers = hours,
            state = hoursState,
            modifier = Modifier.drawVerticalOverlay(),
        )

        NumberPicker(
            current = currentMinute,
            numbers = minutes,
            state = minutesState,
            modifier = Modifier
                .drawVerticalOverlay()
                .padding(start = 32.dp),
        )
    }
}

@OptIn(ExperimentalLazyDsl::class)
@Composable
private fun NumberPicker(
    current: Int,
    numbers: List<Int>,
    state: LazyListState,
    modifier: Modifier = Modifier,
) {
    val sizeInDp = with(DensityAmbient.current) { TimeTextLineHeight.toDp() }

    LazyColumn(
        modifier = modifier.height(sizeInDp.times(5)).width(48.dp),
        state = state,
    ) {
        repeat(2) { item { Text("  ", modifier.height(sizeInDp), style = TimeTextStyle) } }
        items(numbers) { item -> TimeCell(item, current == item) }
        repeat(2) { item { Text("  ", modifier.height(sizeInDp), style = TimeTextStyle) } }
    }
}

@Composable
private fun TimeCell(
    number: Int,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = if (selected) Color.Black else Color.Black.copy(alpha = 0.2f)
    Text(
        "%02d".format(number),
        modifier.height(with(DensityAmbient.current) { TimeTextLineHeight.toDp() }),
        color = color,
        style = TimeTextStyle,
    )
}

@Composable
@Preview(showBackground = true)
private fun TimePickerPreview() {
    TimePicker(
        initialHour = 4,
        initialMinute = 34,
        onHour = {},
        onMinute = {},
    )
}
