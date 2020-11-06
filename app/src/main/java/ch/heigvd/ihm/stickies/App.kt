package ch.heigvd.ihm.stickies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ch.heigvd.ihm.stickies.ui.Model
import ch.heigvd.ihm.stickies.ui.demo
import ch.heigvd.ihm.stickies.ui.freeform.Pane

/**
 * A composable that acts as the main entry point of the application. This is where state should be
 * managed, and top-level navigation between the different destinations should take place.
 */
@Composable
fun App() {
    // Global application state.
    val state = remember { mutableStateOf(Model.demo()) }

    // Actual screens of the application.
    Pane(
        state = state,
    )
}