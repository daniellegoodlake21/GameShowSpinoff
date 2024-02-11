package danielle.projects.gameshowspinoff.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import danielle.projects.gameshowspinoff.model.ColorBarStateItem
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.util.ColorBarState
import danielle.projects.gameshowspinoff.util.GameState
import danielle.projects.gameshowspinoff.util.MoneyLadderTimer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoneyLadderViewModel @Inject constructor(): ViewModel() {

    private val _question = MutableStateFlow(Question(1, "Test Question", 10, 1))

    val question = _question.asStateFlow()

    private val _numericAnswer = MutableStateFlow(-1) // all answers in this game are integers

    private val _correctAnswer = MutableStateFlow(15)

    private val _gameState = MutableStateFlow(GameState.DIAL_IN_ANSWER)
    val gameState = _gameState.asStateFlow()

    private val _totalStepsClimbed = MutableStateFlow(0)
    val totalStepsClimbed = _totalStepsClimbed.asStateFlow()

    private val _money = MutableStateFlow(0.0) // in £ GBP
    val moneyTooltip = MutableStateFlow("£%.2f".format(_money.value))

    private val _lives = MutableStateFlow(25)
    val lives = _lives.asStateFlow()

    private var moneyLadderTimer: MoneyLadderTimer? = null

    // map of which bars being reached or surpassed give which money on receipt of an exact answer
    val moneyCheckpoints: Map<Int, Double> = mapOf(10 to 1.0, 20 to 1.5, 30 to 2.0, 40 to 4.0, 50 to 8.0, 60 to 12.5, 70 to 20.0, 80 to 30.0, 90 to 45.0, 99 to 60.0)

    val tooltip = MutableStateFlow("")

    var colorBarStates = mutableStateListOf<ColorBarStateItem>()

    val setNumericAnswer : (String) -> Unit = { newAnswer ->
        try {
            _numericAnswer.value = newAnswer.trimStart{it == '0'}.toInt()
            setTooltip("Lock In or Cash Out when you are ready!")
        }
        catch (e: Exception) {
            setTooltip("Please enter a whole number.")
        }
    }
    private fun getInitialLadderBars(): MutableList<ColorBarStateItem>
    {
        val stateItems = mutableListOf<ColorBarStateItem>()
        for (i in 0 until 100) {
            stateItems.add(ColorBarStateItem(i, ColorBarState.NOT_YET_REACHED))
        }
        return stateItems
    }

    init {
        colorBarStates.addAll(getInitialLadderBars())
    }

    // set lives at the beginning
    fun setLives(startLives: Int) {
        _lives.value = startLives
    }

    fun addLives(newLives: Int) {
        _lives.value += newLives
    }

    // bank the money when an exact answer is given
    fun bankMoney(moneyCheckpoint: Int) {
        val moneyToBank = moneyCheckpoints[moneyCheckpoint]
        moneyToBank?.let {
            _money.value = moneyToBank
            moneyTooltip.value = "£%.2f".format(_money.value)
        }
    }

    fun loseGame() {
        setGameState(GameState.LOST_GAME)
        setTooltip("Lost Game! Keep half the money.")
        _money.value /= 2 // in this version of the game, you still win half the money you have banked even if you lose
        moneyTooltip.value = "£%.2f".format(_money.value)
    }

    // handles exact answers and losing lives
    fun playerMoveResults() {
        moneyLadderTimer?.livesLostTimer?.start()
    }
    fun playerMove(inputAnswer: String){
        setNumericAnswer(inputAnswer)
        if (_numericAnswer.value > 0) {
            moneyLadderTimer = MoneyLadderTimer(moneyLadderViewModel = this, userNumberOfSteps = _numericAnswer.value, correctNumberOfSteps = _correctAnswer.value)
            moneyLadderTimer?.playerMoveTimer?.start() /* handles the initial climb before losing lives
            and loses the game if and when the player's step count reaches one over the exact answer */
        }
    }

    fun setTooltip(newTooltip: String) {
        tooltip.value = newTooltip
    }

    fun setColorBarState(colorBarPosition: Int, newState: ColorBarState, increment: Boolean = true) {
        colorBarStates[colorBarPosition] = ColorBarStateItem(colorBarPosition, newState)
        if (increment) {
            _totalStepsClimbed.value++
        }
    }

    fun setGameState(newGameState: GameState) {
        _gameState.value = newGameState
        if (newGameState == GameState.DIAL_IN_ANSWER) {
            for (i in 0 until colorBarStates.size) {
                if (colorBarStates[i].colorBarState == ColorBarState.LOST_LIFE_RED) {
                    colorBarStates[i].colorBarState = ColorBarState.PLAYER_INPUT_GRAY
                }
            }
            setTooltip("")
        }
    }

    fun flashGoldCorrect() {
        for (i in 0 until totalStepsClimbed.value) {
            setColorBarState(i, ColorBarState.CORRECT_GOLD, increment = false)
        }
        viewModelScope.launch {
            delay(1500) // 1.5 seconds gold
            for (i in 0 until totalStepsClimbed.value) {
                setColorBarState(i, ColorBarState.PLAYER_INPUT_GRAY, increment = false)
            }
            setGameState(GameState.WAIT_FOR_PLAYER_TO_ASK_FOR_NEXT_QUESTION)
        }


    }

}
