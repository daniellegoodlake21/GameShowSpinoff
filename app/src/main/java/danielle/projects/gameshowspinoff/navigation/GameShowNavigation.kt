package danielle.projects.gameshowspinoff.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import danielle.projects.gameshowspinoff.screen.HomeScreen
import danielle.projects.gameshowspinoff.screen.MoneyLadderScreen
import danielle.projects.gameshowspinoff.screen.QuestionBuilderScreen
import danielle.projects.gameshowspinoff.screen.QuestionSetBuilderScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameShowNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = GameShowScreens.HomeScreen.name){
        composable(GameShowScreens.HomeScreen.name){
            // here, pass where this should lead to
            HomeScreen(navController = navController)
        }
        composable(GameShowScreens.QuestionSetBuilderScreen.name){
            QuestionSetBuilderScreen(navController = navController)
        }
        composable(GameShowScreens.QuestionBuilderScreen.name+ "/{questionSetId}",
            arguments = listOf(navArgument(name = "questionSetId") {type = NavType.IntType})){ backStackEntry ->
            QuestionBuilderScreen(navController = navController, questionSetId = backStackEntry.arguments?.getInt("questionSetId"))
        }
        composable(GameShowScreens.PlayGameScreen.name){
            MoneyLadderScreen(navController = navController)
        }
    }

}