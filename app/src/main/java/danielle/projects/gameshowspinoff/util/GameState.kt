package danielle.projects.gameshowspinoff.util

enum class GameState {
    WAIT_FOR_PLAYER_TO_ASK_FOR_NEXT_QUESTION,
    DIAL_IN_ANSWER,
    PLAYER_MOVE,
    WAIT_FOR_PLAYER_TO_ASK_FOR_RESULTS,
    PLAYER_MOVE_RESULTS,
    WAIT_FOR_FLASH_GOLD_CORRECT,
    LOST_GAME,
    WON_GAME
}