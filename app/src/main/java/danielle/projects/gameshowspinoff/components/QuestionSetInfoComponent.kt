package danielle.projects.gameshowspinoff.components
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import danielle.projects.gameshowspinoff.model.QuestionSet
import danielle.projects.gameshowspinoff.navigation.GameShowScreens
import danielle.projects.gameshowspinoff.screen.QuestionBuilderViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun QuestionSetInfoComponent(navController: NavController, questionBuilderViewModel: QuestionBuilderViewModel, questionSet: QuestionSet) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val input = rememberSaveable{mutableStateOf(questionSet.label)
    }
    val context = LocalContext.current
    Card(colors = CardDefaults.cardColors(containerColor = Color.LightGray), modifier = Modifier
        .fillMaxWidth()
        .height(275.dp)
        .padding(vertical = 48.dp, horizontal = 16.dp)
        .height(100.dp)
        .clickable { /* navigate to question set with list of questions */
        navController.navigate(route = "${GameShowScreens.QuestionBuilderScreen.name}/${questionSet.id}")}) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(value = input.value,
                onValueChange = { newText ->
                    input.value = newText
                    questionBuilderViewModel.updateQuestionSet(questionSet = questionSet.copy(label = input.value))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp), textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = TextUnit(24f, TextUnitType.Sp),
                    fontFamily = FontFamily.Monospace
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    }))

            Spacer(modifier = Modifier.padding(vertical = 32.dp))
            // Reset game button
            Button(colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red, contentColor = Color.White
            ), onClick = { /* Reset game */
                questionBuilderViewModel.resetGame(questionSetId = questionSet.id)
                Toast.makeText(context, "Game Reset", Toast.LENGTH_LONG).show()
            }) {
                Text(text = "Reset Game", style = TextStyle(fontFamily = FontFamily.Monospace))
            }
        }
    }
}