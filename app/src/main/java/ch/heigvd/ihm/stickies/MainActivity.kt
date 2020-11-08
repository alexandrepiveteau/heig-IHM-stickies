package ch.heigvd.ihm.stickies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.setContent
import androidx.core.view.WindowCompat
import dev.chrisbanes.compose.ProvideDisplayInsets

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideDisplayInsets {
                MaterialTheme {
                    App()
                }
            }
        }
    }
}