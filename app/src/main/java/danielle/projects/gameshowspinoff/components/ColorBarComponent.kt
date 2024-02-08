package danielle.projects.gameshowspinoff.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import danielle.projects.gameshowspinoff.R
import danielle.projects.gameshowspinoff.util.ColorBarState


@Composable
@Preview
fun ColorBarComponent(
    barPosition: Int = 1,
    moneyCheckpointValue: Double? = 10.0,
    initialColorBarState: ColorBarState = ColorBarState.NOT_YET_REACHED,
    onChange: () -> Unit = {}
) {

    val color = when(initialColorBarState){
        ColorBarState.NOT_YET_REACHED -> Color(ContextCompat.getColor(LocalContext.current, R.color.dark_blue))
        ColorBarState.LOST_LIFE_RED -> Color.Red
        ColorBarState.PLAYER_INPUT_GRAY -> Color.DarkGray
        ColorBarState.CORRECT_GOLD -> Color(ContextCompat.getColor(LocalContext.current, R.color.gold))
    }
    val isCheckpoint = moneyCheckpointValue != null
    val border =  if (isCheckpoint) BorderStroke(width = 4.dp, color = Color(ContextCompat.getColor(LocalContext.current, R.color.gold)))
    else BorderStroke(width = 1.dp, color = Color.Black)
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(38.dp)
        .border(border),
        color = color) {
        if (isCheckpoint) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "£%.2f".format(moneyCheckpointValue!!),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = TextUnit(24f, TextUnitType.Sp),
                    color = Color.White)
            }
        }
    }
    onChange()
}