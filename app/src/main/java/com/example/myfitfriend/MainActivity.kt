package com.example.myfitfriend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the app, acting as the entry point. This activity sets up
 * the UI using Jetpack Compose and configures edge-to-edge display.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure the activity to render content in edge-to-edge mode
        // where the app content extends into the window insets area.
        enableEdgeToEdge()

        // Set the content of the activity using Jetpack Compose.
        setContent {
            Navigation() // Replace 'Navigation' with your actual composable function
        }
    }

    /**
     * Enable edge-to-edge display where the app content can extend into
     * the areas that might typically be reserved for system bars.
     */
    private fun enableEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, insets.top, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }
}
