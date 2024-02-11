package danielle.projects.gameshowspinoff.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import danielle.projects.gameshowspinoff.model.QuestionSet
import danielle.projects.gameshowspinoff.screen.QuestionBuilderViewModel

private const val MIN_QUESTIONS = 8
@Composable
fun PlayableQuestionSetComponent(questionBuilderViewModel: QuestionBuilderViewModel, questionSet: QuestionSet) {
    val questionCount = questionBuilderViewModel.getQuestionCountInQuestionSet(questionSet)
    Card(colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        modifier = Modifier.fillMaxWidth().height(100.dp).clickable {
        if (questionCount >= MIN_QUESTIONS)
        {
        /* Navigate to play game with this question set */
        } }) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            Text(text = questionSet.label, fontSize = TextUnit(24f, TextUnitType.Sp), fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center)
            Text(text = "$questionCount Questions", textAlign = TextAlign.Center)
        }
    }
}