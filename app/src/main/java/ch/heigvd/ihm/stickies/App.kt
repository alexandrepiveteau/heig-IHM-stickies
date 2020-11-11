package ch.heigvd.ihm.stickies

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ch.heigvd.ihm.stickies.ui.*
import ch.heigvd.ihm.stickies.ui.details.EditStickyOverlay
import ch.heigvd.ihm.stickies.ui.details.NewSticky
import ch.heigvd.ihm.stickies.ui.freeform.Pane
import ch.heigvd.ihm.stickies.ui.freeform.UndoButton
import ch.heigvd.ihm.stickies.ui.help.HelpIcon
import ch.heigvd.ihm.stickies.ui.help.HelpOverlay
import ch.heigvd.ihm.stickies.ui.material.BigGradientButton
import ch.heigvd.ihm.stickies.ui.modifier.overlay
import dev.chrisbanes.compose.navigationBarsPadding

/**
 * A composable that acts as the main entry point of the application. This is where state should be
 * managed, and top-level navigation between the different destinations should take place.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {
    // Global application state.
    val undos = remember { mutableStateOf(Undos()) }
    val state = remember { mutableStateOf(Model.demo()) }
    var adding by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<StickyIdentifier?>(null) }
    var helping by remember { mutableStateOf(false) }

    // Actual screens of the application.
    Box(Modifier, Alignment.Center) {
        Box(Modifier.padding(bottom = 16.dp)) {
            Pane(
                state = state,
                undos = undos,
                onClick = { editing = it },
                Modifier
                    .background(Color.StickiesFakeWhite)
                    .navigationBarsPadding()
                    .fillMaxSize()
            )
        }
        AnimatedVisibility(
            !state.value.categoryOpen,
            Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp),
        ) {
            HelpIcon(
                onClick = { helping = true },
            )
        }
        val offset = animate(if (state.value.categoryOpen) 512.dp else 0.dp,
            spring(stiffness = StiffnessLow, dampingRatio = DampingRatioLowBouncy))
        NewStickyButton(
            onClick = { adding = true },
            modifier = Modifier.align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
                .offset(x = offset),
        )
        AnimatedVisibility(
            visible = undos.value.isNotEmpty,
            modifier = Modifier.align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            UndoButton(
                onClick = {
                    val (newModel, newUndos) = undos.value.applyAll(state.value)
                    undos.value = newUndos
                    state.value = newModel
                }
            )
        }
    }

    // Display a dialog if we're currently adding a new sticky.
    if (adding) {
        NewSticky(
            onCancel = { adding = false },
            onNewSticky = { title, color, alert ->
                state.value = state.value.stickyAdd(title, color, alert, state.value.categoryOpenIndex ?: 0)
                adding = false
            },
            modifier = Modifier.overlay(),
        )
    }

    // Display a dialog if we're currently editing a new sticky.
    editing?.let { id ->
        state.value.stickies[id]?.let { sticky ->
            EditStickyOverlay(
                sticky = sticky,
                onCancel = { editing = null },
                onSave = { title, color, alert ->
                    state.value = state.value.stickyUpdate(id, title, color, alert)
                    editing = null
                },
                modifier = Modifier.overlay(),
            )
        }
    }

    // Display a help page as needed.
    if (helping) {
        HelpOverlay(
            onCancel = { helping = false },
            modifier = Modifier
                .fillMaxSize()
                .overlay(onDismiss = { helping = false }),
        )
    }
}

@Composable
private fun NewStickyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BigGradientButton(
        onClick = onClick,
        icon = vectorResource(R.drawable.ic_action_plus),
        title = "New Sticky",
        from = Color.StickiesSuperGraddyStart,
        to = Color.StickiesSuperGraddyEnd,
        modifier = modifier,
    )
}
