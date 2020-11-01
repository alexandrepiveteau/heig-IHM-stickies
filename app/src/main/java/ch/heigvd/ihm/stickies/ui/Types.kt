package ch.heigvd.ihm.stickies.ui

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import ch.heigvd.ihm.stickies.R

val Archivo = fontFamily(
    font(R.font.archivo_bold, FontWeight.Bold),
    font(R.font.archivo_medium, FontWeight.Medium),
    font(R.font.archivo_regular, FontWeight.Normal),
    font(R.font.archivo_semibold, FontWeight.SemiBold),
)

val GochiHand = fontFamily(
    font(R.font.gochihand_regular, FontWeight.Normal)
)