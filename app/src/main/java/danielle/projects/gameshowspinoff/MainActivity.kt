package danielle.projects.gameshowspinoff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import danielle.projects.gameshowspinoff.navigation.GameShowNavigation
import danielle.projects.gameshowspinoff.ui.theme.GameShowSpinoffTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameShowSpinoffTheme {
                GameShowApp()
            }
        }
    }
}

@Composable
fun GameShowApp(){
    GameShowNavigation()
}