package danielle.projects.gameshowspinoff.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
                    questionBuilderViewModel.updateQuestionSet(questionSet = questionSet.copy(label = input.value))
                }))
        }
    }
}