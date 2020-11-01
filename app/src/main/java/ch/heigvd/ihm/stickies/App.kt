package ch.heigvd.ihm.stickies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch.heigvd.ihm.stickies.ui.StickiesOrange
import ch.heigvd.ihm.stickies.ui.StickiesPink
import ch.heigvd.ihm.stickies.ui.StickiesYellow
import ch.heigvd.ihm.stickies.ui.stickies.Pile

@Composable
fun App() {
    Box(Modifier.fillMaxSize(), alignment = Alignment.Center) {
        val elements = remember {
            val first = ch.heigvd.ihm.stickies.model.Sticky(
                identifier = 1,
                title = "Hello",
                color = Color.StickiesYellow,
                highlighted = false
            )
            val second = ch.heigvd.ihm.stickies.model.Sticky(
                identifier = 2,
                title = "World",
                color = Color.StickiesOrange,
                highlighted = false,
            )
            val third = ch.heigvd.ihm.stickies.model.Sticky(
                identifier = 3,
                title = "This is a test",
                color = Color.StickiesPink,
                highlighted = false,
            )
            listOf(first, second, third)
        }
        Pile(elements)
    }
}