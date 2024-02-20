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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import danielle.projects.gameshowspinoff.components.LifelineGroupComponent
import danielle.projects.gameshowspinoff.components.MoneyLadderComponent
import danielle.projects.gameshowspinoff.components.QuestionComponent
import danielle.projects.gameshowspinoff.navigation.GameShowScreens
import danielle.projects.gameshowspinoff.util.GameState
import kotlin.math.absoluteValue

@Composable
fun MoneyLadderScreen(navController: NavController, questionSetId: Int?){
    val moneyLadderViewModel: MoneyLadderViewModel = hiltViewModel()
    val ladderContents = moneyLadderViewModel.colorBarStates
    val position by moneyLadderViewModel.totalStepsClimbed.collectAsState()
    val tooltip by moneyLadderViewModel.tooltip.collectAsState()
    val gameState by moneyLadderViewModel.gameState.collectAsState()
    val money by moneyLadderViewModel.moneyTooltip.collectAsState()
    val lives by moneyLadderViewModel.lives.collectAsState()
    val question by moneyLadderViewModel.question.collectAsState()

    if (questionSetId != null) {
        if (moneyLadderViewModel.questionCount == -1) {
            moneyLadderViewModel.loadGame(setId = questionSetId)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MoneyLadderComponent(
                ladderState = ladderContents,
                prizeCheckpoints = moneyLadderViewModel.bonusPrizeLadderMap,
                moneyCheckpoints = moneyLadderViewModel.moneyCheckpoints,
                currentPosition = position
            )
            if (gameState == GameState.DIAL_IN_ANSWER)
            {
                QuestionComponent(question = question.questionText, moneyLadderViewModel = moneyLadderViewModel)
            }

            Row(modifier = Modifier.padding(4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                if (gameState == GameState.WAIT_FOR_PLAYER_TO_ASK_FOR_RESULTS) {
                    Button(onClick = { moneyLadderViewModel.playerMoveResults() }) {
                        Text(text ="Reveal Answer", style = TextStyle(fontFamily = FontFamily.Monospace))
                    }
                }
                else if (gameState == GameState.WAIT_FOR_PLAYER_TO_ASK_FOR_NEXT_QUESTION) {
                    Button(onClick = {
                        moneyLadderViewModel.onFinishQuestion()
                        moneyLadderViewModel.setGameState(GameState.DIAL_IN_ANSWER)
                        moneyLadderViewModel.getNextQuestion()}) {
                        Text(text ="Next Question", style = TextStyle(fontFamily = FontFamily.Monospace))
                    }
                }
                else if (gameState == GameState.WON_GAME || gameState == GameState.LOST_GAME) {
                    Column(horizontalAlignment =  Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                        Button(onClick = {
                            moneyLadderViewModel.resetGame()
                            navController.navigate(GameShowScreens.HomeScreen.name)
                        }) {
                            Text(text ="Return to Menu", style = TextStyle(fontFamily = FontFamily.Monospace))
                        }
                    }
                }
                // banked money and lives
                Surface(modifier = Modifier.padding(12.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = tooltip, style = TextStyle(fontFamily = FontFamily.Monospace))

                        Row {
                            Text(text = buildAnnotatedString {
                                withStyle(style = SpanStyle()) {
                                    append("Money: ")
                                }
                                withStyle(style = SpanStyle(fontSize = TextUnit(20f, TextUnitType.Sp), fontWeight = FontWeight.ExtraBold)) {
                                    append(money)
                                }}, style = TextStyle(fontFamily = FontFamily.Monospace,
                                fontSize = TextUnit(18f, TextUnitType.Sp)
                            ))
                            Spacer(modifier = Modifier.padding(horizontal = 20.dp))
                            Text(text = buildAnnotatedString {
                                withStyle(style = SpanStyle()) {
                                    append("Lives: ")
                                }
                                withStyle(style = SpanStyle(fontSize = TextUnit(20f, TextUnitType.Sp), fontWeight = FontWeight.ExtraBold)) {
                                    append("${lives.absoluteValue}")
                                }
                            }, style = TextStyle(fontFamily = FontFamily.Monospace,
                                fontSize = TextUnit(18f, TextUnitType.Sp)
                            ))
                        }
                        Spacer(modifier = Modifier.padding(vertical = 20.dp))
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append("Bonus Prizes: ")
                                if (moneyLadderViewModel.bonusPrizesCollected.value.size > 0) {
                                    withStyle(style = SpanStyle(fontSize = TextUnit(20f, TextUnitType.Sp), fontWeight = FontWeight.ExtraBold)) {
                                        for (prize in moneyLadderViewModel.bonusPrizesCollected.value) {
                                            append("\n$prize")
                                        }
                                    }
                                }
                                else {
                                    append("No Bonus Prizes Yet. Keep Going!")
                                }
                            }})
                    }
                }
                if (gameState == GameState.DIAL_IN_ANSWER) {
                    LifelineGroupComponent(moneyLadderViewModel = moneyLadderViewModel)
                }
            }
        }
    }
}

