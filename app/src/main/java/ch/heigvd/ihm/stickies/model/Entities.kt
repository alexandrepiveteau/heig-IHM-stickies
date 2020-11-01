package ch.heigvd.ihm.stickies.model

import androidx.compose.ui.graphics.Color

data class Category(
    val identifier: Long,
    val title: String,
)

// TODO : Switch to domain-specific colors enums.

data class Sticky(
    val identifier: Long,
    val color: Color,
    val title: String,
    val highlighted: Boolean,
)