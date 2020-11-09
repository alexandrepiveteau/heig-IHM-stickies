package ch.heigvd.ihm.stickies.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Overlay(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        color = Color.Black.copy(alpha = 2 / 3f),
        modifier = modifier.fillMaxSize(),
    ) {
        content()
    }
}