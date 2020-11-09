package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.graphics.Color

/**
 * Adds a background that fills all the available size and intercepts all the touch gestures.
 */
fun Modifier.overlay() =
    this.then(background(Color.Black.copy(alpha = 2 / 3f)))
        .then(fillMaxSize())
        .then(tapGestureFilter {  })