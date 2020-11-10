package ch.heigvd.ihm.stickies.ui.freeform

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun alertText(time: Long): String {
    val accessor = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault())
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .format(accessor).dropLast(3)
}
