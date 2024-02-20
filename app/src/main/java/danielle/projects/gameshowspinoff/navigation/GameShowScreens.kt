package danielle.projects.gameshowspinoff.navigation

enum class GameShowScreens {
    HomeScreen,
    PrizeBuilderScreen,
    QuestionSetBuilderScreen,
    QuestionBuilderScreen,
    QuestionSetPickerScreen,
    PlayGameScreen;

    fun fromRoute(route: String?): GameShowScreens
            = when (route?.substringBefore("/")){
        HomeScreen.name -> HomeScreen
        PrizeBuilderScreen.name -> PrizeBuilderScreen
        QuestionSetBuilderScreen.name -> QuestionSetBuilderScreen
        QuestionBuilderScreen.name -> QuestionBuilderScreen
        QuestionSetPickerScreen.name -> QuestionSetPickerScreen
        PlayGameScreen.name -> PlayGameScreen
        null -> HomeScreen
        else -> throw IllegalArgumentException("Route $route is not recognised.")
    }
}