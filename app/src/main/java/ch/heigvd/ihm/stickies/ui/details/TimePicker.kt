package ch.heigvd.ihm.stickies.ui.details

import android.graphics.Paint
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
    Button(onClick = { onClick(LocalTime.of(Random.nextInt(0, 23), Random.nextInt(0, 59))) }) {
        Text("${time.hour}:${time.minute}")
    }
}

@Composable
fun Clock(
        time: LocalTime,
        modifier: Modifier = Modifier,
) {
    val clockRadius = 200f
    val offset = Offset(clockRadius, clockRadius)
    val angleOffset = 3/2f

    val hoursLengthMult = 0.4f
    val hoursWidth = 10f

    val minutesLengthMult = 0.6f
    val minutesWidth = 10f

    val numbersPosMult = 0.7f

    fun hPartialAngle(min: Int): Float {
        return ((time.minute / 60f) / 6f * PI.toFloat())
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

    Canvas(modifier
            .height(256.dp)
            .width(256.dp),
    ) {
        // Outer circle
        drawCircle(color = Color(0xFF262626), radius = clockRadius, center = offset)
        drawCircle(color = Color(0xFFFFFFFF), radius = 0.8f * clockRadius, center = offset)

        // draw numbers
        drawIntoCanvas { canvas ->
            for (x in 1 until 13)
                canvas.nativeCanvas.drawText("${x}", offset.x + numbersPosMult * clockRadius * cos(hTruncatedAngle(x)), offset.y + numbersPosMult * clockRadius * sin(hTruncatedAngle(x)), Paint())
        }
        // Minutes hand
        drawLine(color = Color(0xFFC22D2D), start = offset, end = offset.plus(Offset(cos(mAngle(time.minute)), sin(mAngle(time.minute))).times(minutesLengthMult * clockRadius)), strokeWidth = minutesWidth)

        // Hours hand
        drawLine(color = Color(0xFF262626), start = offset, end = offset.plus(Offset(cos(hAngle(time.hour, time.minute)), sin(hAngle(time.hour, time.minute))).times(hoursLengthMult * clockRadius)), strokeWidth = hoursWidth)

        // Center dot
        drawCircle(color = Color(0xFF262626), radius = 0.075f * clockRadius, center = offset)
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