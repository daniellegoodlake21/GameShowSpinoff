package danielle.projects.gameshowspinoff.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import danielle.projects.gameshowspinoff.R
import danielle.projects.gameshowspinoff.components.ColorBarComponent
import danielle.projects.gameshowspinoff.model.ColorBarStateItem
import danielle.projects.gameshowspinoff.navigation.GameShowScreens
import danielle.projects.gameshowspinoff.util.ColorBarState

@Composable
fun HomeScreen(navController: NavController) {
    val sampleMoneyLadder: MutableList<ColorBarStateItem> = mutableListOf()
    for (i in 0..20) {
        if (i < 10) {
            sampleMoneyLadder.add(ColorBarStateItem(i, ColorBarState.PLAYER_INPUT_GRAY))
        }
        else if (i < 13) {
            sampleMoneyLadder.add(ColorBarStateItem(i, ColorBarState.LOST_LIFE_RED))
        }
        else {
            sampleMoneyLadder.add(ColorBarStateItem(i, ColorBarState.NOT_YET_REACHED))
        }
    }
    // background rainbow screen
    val rainbowColors = Brush.verticalGradient(listOf(Color.Red,
        Color(LocalContext.current.getColor(R.color.orange)),
        Color.Yellow,
        Color.Cyan,
        Color.Blue,
        Color.Magenta))
    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = rainbowColors),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
            Text(text = "LIMITED WIN", color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = TextUnit(42f, TextUnitType.Sp))
            Spacer(modifier = Modifier.padding(32.dp))
            LazyColumn(Modifier.height(180.dp).padding(horizontal = 120.dp)){
                items(sampleMoneyLadder) { item ->
                    ColorBarComponent(barPosition = item.barPosition,
                        moneyCheckpointValue = if (item.barPosition == 4) 1.0 else if (item.barPosition == 9) 1.5 else null,
                        bonusPrize = "",
                        initialColorBarState = item.colorBarState)
                }
            }
            Spacer(modifier = Modifier.padding(32.dp))
            Button(border = BorderStroke(2.dp, Color.Black), colors = ButtonDefaults.buttonColors(
                containerColor = Color(
                    LocalContext.current.getColor(
                        R.color.dark_blue
                    )
                )
            ),
                modifier = Modifier.height(100.dp),
                shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                onClick = { /* Navigate to question set picker (play mode) screen */
                navController.navigate(route = GameShowScreens.QuestionSetPickerScreen.name)}) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = "Play Game",
                    Modifier
                        .padding(start = 0.dp, top = 12.dp, bottom = 12.dp, end = 6.dp)
                        .size(100.dp)
                )
                Text("Play", fontFamily = FontFamily.Monospace, fontSize = TextUnit(28f, TextUnitType.Sp))
            }
            Spacer(modifier = Modifier.padding(32.dp))
            Button(border = BorderStroke(2.dp, Color.Black), colors = ButtonDefaults.buttonColors(
                containerColor = Color(
                    LocalContext.current.getColor(
                        R.color.gold
                    )
                )
            ),
                modifier = Modifier.height(100.dp),
                shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                onClick = { /* Navigate to question set builder (editor mode) screen */
                navController.navigate(route = GameShowScreens.QuestionSetBuilderScreen.name )}) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Question Builder",
                    Modifier
                        .padding(start = 0.dp, top = 12.dp, bottom = 12.dp, end = 6.dp)
                        .size(100.dp)
                )
                Text("Edit Questions", fontFamily = FontFamily.Monospace, fontSize = TextUnit(22f, TextUnitType.Sp))

            }
        Spacer(modifier = Modifier.padding(16.dp))
        Button(border = BorderStroke(2.dp, Color.Black), colors = ButtonDefaults.buttonColors(
            containerColor = Color(
                LocalContext.current.getColor(
                    R.color.gold
                )
            )
        ),
            modifier = Modifier.height(100.dp),
            shape = RoundedCornerShape(corner = CornerSize(5.dp)),
            onClick = { /* Navigate to prize builder (editor mode) screen */
                navController.navigate(route = GameShowScreens.PrizeBuilderScreen.name )}) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Prize Builder",
                Modifier
                    .padding(start = 0.dp, top = 12.dp, bottom = 12.dp, end = 6.dp)
                    .size(100.dp)
            )
            Text("Settings", fontFamily = FontFamily.Monospace, fontSize = TextUnit(22f, TextUnitType.Sp))
        }
    }
}