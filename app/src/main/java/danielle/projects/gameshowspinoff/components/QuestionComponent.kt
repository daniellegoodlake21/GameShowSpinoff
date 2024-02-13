package danielle.projects.gameshowspinoff.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import danielle.projects.gameshowspinoff.R
import danielle.projects.gameshowspinoff.screen.MoneyLadderViewModel
import danielle.projects.gameshowspinoff.util.GameState


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuestionComponent(
    question: String,
    moneyLadderViewModel: MoneyLadderViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val gradient = listOf(Color.Cyan, Color.Blue)
    val brush = remember {
        Brush.linearGradient(
            colors = gradient
        )
    }
    var input by rememberSaveable {
        mutableStateOf("")
    }
    // Question text
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = question, style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                fontSize = TextUnit(20f, TextUnitType.Sp)
            )
        )
    }
    // input field and label
    Row(
        modifier = Modifier.padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = "Answer: ", style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = TextUnit(24f, TextUnitType.Sp)))

        OutlinedTextField(
            value = input,
            textStyle = TextStyle(brush = brush, fontFamily = FontFamily.Monospace, fontSize = TextUnit(22f, TextUnitType.Sp)),
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onSend = {keyboardController?.hide()}, onDone = { keyboardController?.hide() },
                onGo = {keyboardController?.hide()}, onNext = {keyboardController?.hide()}),
            onValueChange = {newAnswer -> input = newAnswer.trimStart('0')}
            )

    }
    // lock in and cash out buttons
    Row(modifier = Modifier.padding(4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(colors = ButtonDefaults.buttonColors(
            containerColor = Color(
                ContextCompat.getColor(
                    LocalContext.current, R.color.dark_blue
                )
            ), contentColor = Color.White
        ), onClick = { moneyLadderViewModel.playerMove(inputAnswer  = input) }) {
            Text(text = "Lock In", style = TextStyle(fontFamily = FontFamily.Monospace))
        }
        Spacer(modifier = Modifier.padding(12.dp))
        Button(colors = ButtonDefaults.buttonColors(
            containerColor = Color(
                ContextCompat.getColor(
                    LocalContext.current, R.color.gold
                )
            )
        ), onClick = { moneyLadderViewModel.setGameState(GameState.WON_GAME) }) {
            Text(text = "Cash Out", style = TextStyle(fontFamily = FontFamily.Monospace))
        }
    }
}