package ch.heigvd.ihm.stickies.ui.details

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import java.time.LocalTime
import java.time.LocalTime.MIDNIGHT
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun TimePicker(
        time: LocalTime,
        onClick: (LocalTime) -> Unit,
        modifier: Modifier = Modifier,
) {
    Row(modifier) {
        Button(onClick = { onClick(LocalTime.of((time.hour + 1) % 24, time.minute % 60)) }) {
            Text("${time.hour}")
        }

        Button(onClick = { onClick(LocalTime.of(time.hour % 24, (time.minute + 1) % 60)) }) {
            Text("${time.minute}")
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
    val hoursLengthMult = 0.4f
    val hoursWidth = 10f

    // Minutes hand properties
    val minutesLengthMult = 0.6f
    val minutesWidth = 10f

    // Numbers properties
    val numbersPosMult = 0.7f
    val textSize = 0.15f * clockRadius


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

    // Draw on the canvas
    Canvas(
            modifier
                    .height((2 * clockRadius).dp)
                    .width((2 * clockRadius).dp),
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
            paint.setTextSize(textSize)
            paint.setTypeface(Typeface.DEFAULT_BOLD)
            paint.isAntiAlias = true

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
                                cos(mAngle(time.minute)),
                                sin(mAngle(time.minute))
                        ).times(minutesLengthMult * clockRadius)
                ),
                strokeWidth = minutesWidth,
        )

        // Hours hand
        drawLine(
                color = Color(0xFF262626),
                start = offset,
                end = offset.plus(
                        Offset(
                                cos(hAngle(time.hour, time.minute)),
                                sin(hAngle(time.hour, time.minute))
                        ).times(hoursLengthMult * clockRadius)
                ),
                strokeWidth = hoursWidth,
        )

        // Center dot
        drawCircle(
                color = Color(0xFF262626),
                radius = 0.075f * clockRadius,
                center = offset,
        )
    }
}

@Composable
@Preview
private fun TimePickerPreview() {
    Column(Modifier.background(Color.White).padding(16.dp)) {
        val (time, setTime) =
                remember { mutableStateOf(MIDNIGHT) }
        TimePicker(
                time = time,
                onClick = setTime,
        )
        Clock(
                time = time,
        )
    }
}