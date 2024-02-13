package danielle.projects.gameshowspinoff.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import danielle.projects.gameshowspinoff.R
import danielle.projects.gameshowspinoff.components.QuestionSetInfoComponent
import danielle.projects.gameshowspinoff.model.QuestionSet

@Composable
fun QuestionSetBuilderScreen(navController: NavController) {
    val listState = rememberLazyListState()
    val questionBuilderViewModel: QuestionBuilderViewModel = hiltViewModel()
    val questionSets by questionBuilderViewModel.questionSetList.collectAsState()
    // List of question sets to edit - then to play a game you use one question set
        LazyColumn(state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()){
            items(items = questionSets) { item ->
                QuestionSetInfoComponent(navController = navController, questionBuilderViewModel = questionBuilderViewModel, questionSet = item)
        }
        item{
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // new question set button
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = Color(
                        ContextCompat.getColor(
                            LocalContext.current, R.color.gold
                        )
                    ), contentColor = Color.White
                ), onClick = { questionBuilderViewModel.addQuestionSet(QuestionSet(label = "")) }) {
                    Text(text = "New Question Set", style = TextStyle(fontFamily = FontFamily.Monospace))
                }
                Spacer(modifier = Modifier.padding(16.dp))
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