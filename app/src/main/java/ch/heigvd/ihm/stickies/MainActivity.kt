package ch.heigvd.ihm.stickies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.platform.setContent
import androidx.core.view.WindowCompat
import dev.chrisbanes.compose.ProvideDisplayInsets

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideDisplayInsets {
                App()
            }
        }
    }
}