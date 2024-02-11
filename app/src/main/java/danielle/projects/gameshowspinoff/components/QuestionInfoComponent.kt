package danielle.projects.gameshowspinoff.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.screen.QuestionBuilderViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun QuestionInfoComponent(questionBuilderViewModel: QuestionBuilderViewModel, question: Question? = Question(id = 1, questionText = "How many years since the year 2000?", correctAnswer = 24, questionsSetId = 1)) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Card(colors = CardDefaults.cardColors(containerColor = Color.LightGray), modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            if (question != null) {
                OutlinedTextField(
                    value = TextFieldValue(question.questionText),
                    onValueChange = { newText ->
                        if (newText.text.isNotEmpty()) {

                            question.questionText = newText.text
                            questionBuilderViewModel.updateQuestion(question = question)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onSend = { keyboardController?.hide() },
                        onDone = { keyboardController?.hide() },
                        onGo = { keyboardController?.hide() },
                        onNext = { keyboardController?.hide() })
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = TextFieldValue(question.correctAnswer.toString()),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = TextUnit(16f, TextUnitType.Sp)
                    ),
                    modifier = Modifier
                        .width(100.dp)
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onSend = { keyboardController?.hide() },
                        onDone = { keyboardController?.hide() },
                        onGo = { keyboardController?.hide() },
                        onNext = { keyboardController?.hide() }),
                    onValueChange = { newCorrectAnswer ->
                        question.correctAnswer = newCorrectAnswer.text.toInt()
                        questionBuilderViewModel.updateQuestion(question = question)
                    }
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Button(onClick = { questionBuilderViewModel.removeQuestion(question) }) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete Question",
                        Modifier
                            .padding(12.dp)
                            .size(32.dp)
                    )
                }
            }
        }
    }
}