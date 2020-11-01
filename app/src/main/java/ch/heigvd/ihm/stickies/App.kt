package ch.heigvd.ihm.stickies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ch.heigvd.ihm.stickies.ui.stickies.Sticky

@Composable
fun App() {
    Box(Modifier.fillMaxSize(), alignment = Alignment.Center) {
        val (visible, setVisible) = remember { mutableStateOf(true) }
        Sticky(
            text = "Hello world",
            modifier = Modifier.clickable(onClick = { setVisible(!visible) }),
            highlighted = visible,
        )
    }
}