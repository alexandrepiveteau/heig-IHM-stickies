package ch.heigvd.ihm.stickies.model

data class Category(
    val identifier: Long,
    val title: String,
)

data class Sticky(
    val identifier: Long,
    val title: String,
)