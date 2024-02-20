package danielle.projects.gameshowspinoff.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import danielle.projects.gameshowspinoff.screen.MoneyLadderViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LifelineGroupComponent(moneyLadderViewModel: MoneyLadderViewModel) {
    val oddOrEvenEnabled by moneyLadderViewModel.oddOrEvenState.collectAsState()
    val rangeEnabled by moneyLadderViewModel.rangeState.collectAsState()
    val moreThanEnabled by moneyLadderViewModel.moreThanState.collectAsState()
    var moreThanInput by rememberSaveable {
        mutableStateOf("")
    }

    val lifelineResult by moneyLadderViewModel.lifelineResultValue.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Lifelines")
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Button(onClick = { moneyLadderViewModel.useOddOrEvenLifeline() },
                enabled = oddOrEvenEnabled){
                Text("Odd Or Even")
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            Button(onClick = { moneyLadderViewModel.useRangeLifeline() },
                enabled = rangeEnabled){
                Text("Range")
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Button(onClick = { moneyLadderViewModel.useMoreThan() },
                enabled = moreThanEnabled){
                Text("More Than")
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text("More Than: ")
            OutlinedTextField(modifier = Modifier.width(100.dp), value = moreThanInput, onValueChange = { value ->
                moreThanInput = value
                moneyLadderViewModel.setMoreThanLifelineValue(value)},
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onSend = {keyboardController?.hide()}, onDone = { keyboardController?.hide() },
                    onGo = {keyboardController?.hide()}, onNext = {keyboardController?.hide()})
            )
        }
        Text(text = lifelineResult, fontFamily = FontFamily.Monospace, fontSize = TextUnit(18f, TextUnitType.Sp))
    }
}