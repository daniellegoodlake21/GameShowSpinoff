package danielle.projects.gameshowspinoff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import danielle.projects.gameshowspinoff.screen.HomeScreen
import danielle.projects.gameshowspinoff.ui.theme.GameShowSpinoffTheme

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
    HomeScreen()
}