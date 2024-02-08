package danielle.projects.gameshowspinoff.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import danielle.projects.gameshowspinoff.components.MoneyLadderComponent
import danielle.projects.gameshowspinoff.util.GameState

@Composable
@Preview(showBackground=true)
fun MoneyLadderScreen(startLives: Int = 25){
    val moneyLadderViewModel: MoneyLadderViewModel = viewModel()
    val ladderContents = moneyLadderViewModel.colorBarStates
    val position by moneyLadderViewModel.totalStepsClimbed.collectAsState()
    val tooltip by moneyLadderViewModel.tooltip.collectAsState()
    val gameState by moneyLadderViewModel.gameState.collectAsState()
    val money by moneyLadderViewModel.moneyTooltip.collectAsState()

    moneyLadderViewModel.setLives(startLives = startLives)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MoneyLadderComponent(
            ladderState = ladderContents,
            moneyCheckpoints = moneyLadderViewModel.moneyCheckpoints,
            currentPosition = position
        )
        if (gameState == GameState.DIAL_IN_ANSWER)
        {
            Row(modifier = Modifier.padding(4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { moneyLadderViewModel.playerMove(22, 22) }) {
                    Text(text = "Exact Cash", style = TextStyle(fontFamily = FontFamily.Monospace))
                }
                Spacer(modifier = Modifier.padding(2.dp))
                Button(onClick = { moneyLadderViewModel.playerMove(5, 3) }) {
                    Text(text = "Game Over", style = TextStyle(fontFamily = FontFamily.Monospace))
                }
                Spacer(modifier = Modifier.padding(2.dp))
                Button(onClick = { moneyLadderViewModel.playerMove(12, 50) }) {
                    Text(text = "Lose Lives", style = TextStyle(fontFamily = FontFamily.Monospace))
                }
            }
        }

        Row(modifier = Modifier.padding(4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            if (gameState == GameState.WAIT_FOR_PLAYER_TO_ASK_FOR_RESULTS)
            {
                Button(onClick = { moneyLadderViewModel.playerMoveResults() }) {
                    Text(text ="Reveal Answer", style = TextStyle(fontFamily = FontFamily.Monospace))
                }
            }

            Surface(modifier = Modifier.padding(12.dp)) {
                Column {
                    Text(text = tooltip, style = TextStyle(fontFamily = FontFamily.Monospace))
                    Text(text = money, style = TextStyle(fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = TextUnit(22f, TextUnitType.Sp)
                    ))
                }
            }
        }
    }
}
