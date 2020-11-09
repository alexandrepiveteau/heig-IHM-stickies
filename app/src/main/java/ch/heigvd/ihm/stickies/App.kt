package ch.heigvd.ihm.stickies

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ch.heigvd.ihm.stickies.ui.*
import ch.heigvd.ihm.stickies.ui.details.TimePicker
import ch.heigvd.ihm.stickies.ui.freeform.Pane
import ch.heigvd.ihm.stickies.ui.freeform.UndoButton
import ch.heigvd.ihm.stickies.ui.material.GradientButton
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

    // Actual screens of the application.
    Box(Modifier, Alignment.Center) {
        Box(Modifier.padding(bottom = 16.dp)) {
            Pane(
                state = state,
                undos = undos,
                Modifier
                    .background(Color.StickiesFakeWhite)
                    .navigationBarsPadding()
                    .fillMaxSize()
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
        Dialog(onDismissRequest = { /* Ignored. */ }) {
            Column() {
                StickyDetails(
                    modifier = Modifier
                        .background(Color.White),
                )

                Button(
                    onClick = { adding = false },
                    modifier = Modifier
                        .background(Color.White),
                ) {
                    Text("Back to Home.")
                }

            }
        }
    }
}

private val NewStickyButtonTextStyle = TextStyle(
    fontFamily = Archivo,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
)

@Composable
private fun NewStickyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GradientButton(
        onClick = onClick,
        borderSize = 4.dp,
        modifier = modifier.height(64.dp),
    ) {
        Spacer(Modifier.width(16.dp))
        // TODO : Fix this icon scaling.
        Icon(
            vectorResource(R.drawable.ic_action_plus),
            Modifier.drawLayer(scaleX = 1.25f, scaleY = 1.25f)
        )
        Spacer(Modifier.width(16.dp))
        Text("New Sticky", style = NewStickyButtonTextStyle)
        Spacer(Modifier.width(16.dp))
    }
}