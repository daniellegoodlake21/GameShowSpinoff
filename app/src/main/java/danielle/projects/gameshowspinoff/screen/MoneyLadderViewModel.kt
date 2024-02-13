package danielle.projects.gameshowspinoff.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import danielle.projects.gameshowspinoff.model.ColorBarStateItem
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.repository.QuestionRepository
import danielle.projects.gameshowspinoff.util.ColorBarState
import danielle.projects.gameshowspinoff.util.GameState
import danielle.projects.gameshowspinoff.util.MoneyLadderTimer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class MoneyLadderViewModel @Inject constructor(private val repository: QuestionRepository): ViewModel() {

    var questionCount: Int = 0

    private val _question = MutableStateFlow(Question(1, "Test Question", 1, 1))

    val question = _question.asStateFlow()

    private val _numericAnswer = MutableStateFlow(-1) // all answers in this game are integers

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
    val moneyCheckpoints: MutableMap<Int, Double> = mutableMapOf()

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
        for (i in 0 until 500) {
            stateItems.add(ColorBarStateItem(i, ColorBarState.NOT_YET_REACHED))
        }
        return stateItems
    }

    fun getNextQuestion(questionSetId: Int, firstQuestion: Boolean = false) {
        if (!firstQuestion) {
            questionCount++
        }
        viewModelScope.launch {
            repository.getAllQuestionsInSet(questionSetId).collect{ questionsList ->
                if (questionsList.size <= questionCount) {
                    setGameState(GameState.WON_GAME)
                    setTooltip("You completed all the questions and won ${moneyTooltip.value}! Congratulations!")
                }
                else
                {
                    _question.value = questionsList[questionCount]
                }
            }
        }
    }
    init {
        var checkpoint = 20
        var cash = 2.0
        while (checkpoint < 500) {
            moneyCheckpoints[checkpoint] = cash
            checkpoint += 20
            if (cash < 30) {
                cash *= 2
            }
            else if (cash < 72) {
                cash *= 1.5
                cash = ceil(cash)
            }
            else if (cash < 111)
            {
                cash *= 1.2
                cash = ceil(cash)
            }
            else {
                cash += 1
            }
        }
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
            moneyLadderTimer = MoneyLadderTimer(moneyLadderViewModel = this, userNumberOfSteps = _numericAnswer.value, correctNumberOfSteps = _question.value.correctAnswer)
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
