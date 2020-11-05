@file:Suppress("FunctionName")

package ch.heigvd.ihm.stickies.ui.freeform

inline class ScrollState(val amount: Float)

fun NoScroll() = ScrollState(0f)

fun Scrolled(amount: Float) = ScrollState(amount)