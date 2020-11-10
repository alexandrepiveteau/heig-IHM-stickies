package ch.heigvd.ihm.stickies.ui.details

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.animation.animate
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
    hours: Int,
    minutes: Int,
    modifier: Modifier = Modifier,
) {
    val hAngle = animate(hAngle(hours, minutes))
    val mAngle = animate(mAngle(minutes))

    // Draw on the canvas
    Canvas(modifier.preferredSize(200.dp)) {

        val clockRadius = minOf(size.height, size.width) / 2f

        // General clock properties
        val offset = Offset(clockRadius, clockRadius)

        // Hours hand properties
        val hoursLengthMult = 0.35f
        val hoursWidth = 0.04f * clockRadius

        // Minutes hand properties
        val minutesLengthMult = 0.55f
        val minutesWidth = 0.04f * clockRadius

        // Numbers properties
        val numbersPosMult = 0.675f
        val textSize = 0.175f * clockRadius

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
                val rect = Rect()
                paint.getTextBounds(x.toString(), 0, x.toString().length, rect)
                canvas.nativeCanvas.drawText(
                    "$x",
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
            end = offset + (Offset(cos(mAngle), sin(mAngle)) * minutesLengthMult * clockRadius),
            strokeWidth = minutesWidth,
            cap = StrokeCap.Round,
        )

        // Hours hand
        drawLine(
            color = Color(0xFF262626),
            start = offset,
            end = offset + (Offset(cos(hAngle), sin(hAngle)) * hoursLengthMult * clockRadius),
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

// Math functions

private const val AngleOffset = 3 / 2f

private fun hPartialAngle(min: Int): Float {
    return ((min / 60f) / 6f * PI.toFloat())
}

private fun hTruncatedAngle(hour: Int): Float {
    return (hour / 6f + AngleOffset) * PI.toFloat()
}

private fun hAngle(hour: Int, min: Int): Float {
    return hTruncatedAngle(hour) + hPartialAngle(min)
}

private fun mAngle(min: Int): Float {
    return (min / 30f + AngleOffset) * PI.toFloat()
}