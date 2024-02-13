package danielle.projects.gameshowspinoff.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import danielle.projects.gameshowspinoff.R
import danielle.projects.gameshowspinoff.components.MIN_QUESTIONS
import danielle.projects.gameshowspinoff.components.PlayableQuestionSetComponent

@Composable
fun QuestionSetPickerScreen(navController: NavController) {
    val listState = rememberLazyListState()
    val questionBuilderViewModel: QuestionBuilderViewModel = hiltViewModel()
    val questionSets by questionBuilderViewModel.questionSetList.collectAsState()
    val questionCountList by questionBuilderViewModel.questionCountInQuestionSetMap.collectAsState()
    // List of question sets to edit - then to play a game you use one question set
    LazyColumn(state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()){
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Pick a Question Set!", fontSize = TextUnit(24f, TextUnitType.Sp), fontFamily = FontFamily.Monospace)
                Text(text = "Remember you can only pick sets containing a minimum of $MIN_QUESTIONS questions.", fontFamily = FontFamily.Monospace)
            }
        }
            items(items = questionSets) { item ->
                PlayableQuestionSetComponent(questionCountList = questionCountList, navController = navController, questionBuilderViewModel = questionBuilderViewModel, questionSet = item)
        }
        item{
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Back button
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = Color(
                        ContextCompat.getColor(
                            LocalContext.current, R.color.dark_blue
                        )
                    ), contentColor = Color.White
                ), onClick = { /* Navigate to home screen */
                    navController.popBackStack()}) {
                    Text(text = "Back", style = TextStyle(fontFamily = FontFamily.Monospace))
                }
            }

        }
    }
}