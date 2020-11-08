package ch.heigvd.ihm.stickies.ui.details

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.animation.animate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun TimePicker(
        time: LocalTime = LocalTime.now(),
        modifier: Modifier = Modifier,
) {
    Row(modifier) {
        val hours = (0..23).distinct()
        val hoursState = rememberLazyListState(initialFirstVisibleItemIndex = time.hour)

        val minutes = (0..59).distinct()
        val minutesState = rememberLazyListState(initialFirstVisibleItemIndex = time.minute)

        Clock(
                time = LocalTime.of(
                    hours[hoursState.firstVisibleItemIndex],
                    minutes[minutesState.firstVisibleItemIndex],
                ),
                modifier = modifier,
        )

        NumberPicker(
                numbers = hours,
                state = hoursState,
        )

        NumberPicker(
                numbers = minutes,
                state = minutesState,
        )
    }
}

@Composable
fun NumberPicker(
        numbers: List<Int>,
        state: LazyListState,
        modifier: Modifier = Modifier.height(260.dp),
        fontSize: TextUnit = 40.sp,
) {
    val nanList = listOf(-1, -1)
    val items = nanList + numbers + nanList
    LazyColumnForIndexed(
            items = items,
            modifier = modifier,
            state = state,
    ) { index, item ->
        if (index == 0 || index == 1 || index == items.size - 2 || index == items.size - 1) {
            Text("  ", fontSize = fontSize)
        } else {
            Text("%02d".format(item), fontSize = fontSize)
        }
    }
}

@Composable
fun Clock(
        time: LocalTime,
        modifier: Modifier = Modifier,
) {
    // General clock properties
    val clockRadius = 200f
    val offset = Offset(clockRadius, clockRadius)
    val angleOffset = 3 / 2f

    // Hours hand properties
    val hoursLengthMult = 0.35f
    val hoursWidth = 7f

    // Minutes hand properties
    val minutesLengthMult = 0.55f
    val minutesWidth = 7f

    // Numbers properties
    val numbersPosMult = 0.675f
    val textSize = 0.175f * clockRadius


    // Math functions
    fun hPartialAngle(min: Int): Float {
        return ((min / 60f) / 6f * PI.toFloat())
    }

    fun hTruncatedAngle(hour: Int): Float {
        return (hour / 6f + angleOffset) * PI.toFloat()
    }

    fun hAngle(hour: Int, min: Int): Float {
        return hTruncatedAngle(hour) + hPartialAngle(min)
    }

    fun mAngle(min: Int): Float {
        return (min / 30f + angleOffset) * PI.toFloat()
    }

    val hAngle = animate(hAngle(time.hour, time.minute))
    val mAngle = animate(mAngle(time.minute))

    // Draw on the canvas
    Canvas(
            modifier
                    .height(152.dp)
                    .width(152.dp),
    ) {
        // Outer circle
        drawCircle(
                color = Color(0xFF262626),
                radius = clockRadius,
                center = offset,
        )
        drawCircle(
                color = Color(0xFFFFFFFF),
                radius = 0.8f * clockRadius,
                center = offset,
        )

        // draw numbers
        drawIntoCanvas { canvas ->
            // Text styling.
            val paint = Paint()
            paint.textSize = textSize
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.isAntiAlias = true
            paint.color = Color(0xFF262626).toArgb()

            for (x in 1 until 13) {
                var rect = Rect()
                paint.getTextBounds(x.toString(), 0, x.toString().length, rect)
                canvas.nativeCanvas.drawText(
                        "${x}",
                        offset.x + numbersPosMult * clockRadius * cos(hTruncatedAngle(x)) - rect.right / 2f,
                        offset.y + numbersPosMult * clockRadius * sin(hTruncatedAngle(x)) - rect.top / 2f,
                        paint,
                )
            }
        }
        // Minutes hand
        drawLine(
                color = Color(0xFFC22D2D),
                start = offset,
                end = offset.plus(
                        Offset(
                                cos(mAngle),
                                sin(mAngle)
                        ).times(minutesLengthMult * clockRadius)
                ),
                strokeWidth = minutesWidth,
                cap = StrokeCap.Round,
        )

        // Hours hand
        drawLine(
                color = Color(0xFF262626),
                start = offset,
                end = offset.plus(
                        Offset(
                                cos(hAngle),
                                sin(hAngle),
                        ).times(hoursLengthMult * clockRadius)
                ),
                strokeWidth = hoursWidth,
                cap = StrokeCap.Round,
        )

        // Center dot
        drawCircle(
                color = Color(0xFF262626),
                radius = 0.06f * clockRadius,
                center = offset,
        )
    }
}

@Composable
@Preview
private fun TimePickerPreview() {
    Row(Modifier.background(Color.White)) {
        TimePicker(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
        )
    }
}