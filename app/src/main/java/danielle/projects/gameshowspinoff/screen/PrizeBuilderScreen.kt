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
import danielle.projects.gameshowspinoff.components.PrizeInfoComponent
import danielle.projects.gameshowspinoff.model.Prize

@Composable
fun PrizeBuilderScreen(navController: NavController) {
    val listState = rememberLazyListState()
    val prizeBuilderViewModel: PrizeBuilderViewModel = hiltViewModel()
    val prizeList by prizeBuilderViewModel.prizeList.collectAsState()
    val context = LocalContext.current
    LazyColumn(state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
        items(items = prizeList) { item ->
            PrizeInfoComponent(prizeBuilderViewModel = prizeBuilderViewModel, prize = item)
        }
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // new prize button
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = Color(
                        ContextCompat.getColor(
                            LocalContext.current, R.color.gold
                        )
                    ), contentColor = Color.White
                ), onClick = { prizeBuilderViewModel.addPrize(Prize(prizeTitle = "")) }) {
                    Text(text = "New Prize", style = TextStyle(fontFamily = FontFamily.Monospace))
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
