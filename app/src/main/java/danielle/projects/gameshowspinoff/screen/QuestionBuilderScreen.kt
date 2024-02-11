package danielle.projects.gameshowspinoff.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import danielle.projects.gameshowspinoff.R
import danielle.projects.gameshowspinoff.components.QuestionInfoComponent
import danielle.projects.gameshowspinoff.model.QuestionSet

@Composable
fun QuestionBuilderScreen(questionSet: QuestionSet) {
    val listState = rememberLazyListState()
    val questionBuilderViewModel: QuestionBuilderViewModel = viewModel()
    questionBuilderViewModel.getQuestionsListByQuestionSet(questionSet)
    val questions by questionBuilderViewModel.currentQuestionList.collectAsState()
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // List of questions to edit
        LazyColumn(state = listState,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()){
            items(questions){ item ->
            QuestionInfoComponent(questionBuilderViewModel = questionBuilderViewModel, question = item)
        }}
        // Back button
        Button(colors = ButtonDefaults.buttonColors(
            containerColor = Color(
                ContextCompat.getColor(
                    LocalContext.current, R.color.dark_blue
                )
            ), contentColor = Color.White
        ), onClick = { /* Navigate to question set list screen */ }) {
            Text(text = "Back", style = TextStyle(fontFamily = FontFamily.Monospace))
        }
    }


}