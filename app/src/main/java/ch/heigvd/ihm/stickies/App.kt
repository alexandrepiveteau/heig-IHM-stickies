package ch.heigvd.ihm.stickies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ch.heigvd.ihm.stickies.ui.stickies.Pile
import ch.heigvd.ihm.stickies.ui.stickies.Sticky

@Composable
fun App() {
    Box(Modifier.fillMaxSize(), alignment = Alignment.Center) {
        val (visible, setVisible) = remember { mutableStateOf(true) }
        val elements = remember {
            val first = ch.heigvd.ihm.stickies.model.Sticky(
                identifier = 1,
                title = "Hello",
                highlighted = false
            )
            val second = ch.heigvd.ihm.stickies.model.Sticky(
                identifier = 2,
                title = "World",
                highlighted = false,
            )
            val third = ch.heigvd.ihm.stickies.model.Sticky(
                identifier = 3,
                title = "This is a test",
                highlighted = false,
            )
            listOf(first, second, third)
        }
        Pile(elements)
        //Sticky(
        //    text = "Hello world",
        //    modifier = Modifier.clickable(onClick = { setVisible(!visible) }),
        //    highlighted = visible,
        //)
    }
}