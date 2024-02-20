package danielle.projects.gameshowspinoff.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import danielle.projects.gameshowspinoff.model.ColorBarStateItem
import danielle.projects.gameshowspinoff.model.Prize
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.model.SaveGameData
import danielle.projects.gameshowspinoff.repository.QuestionRepository
import danielle.projects.gameshowspinoff.util.ColorBarState
import danielle.projects.gameshowspinoff.util.GameState
import danielle.projects.gameshowspinoff.util.MoneyLadderTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class MoneyLadderViewModel @Inject constructor(private val repository: QuestionRepository): ViewModel() {

    val oddOrEvenState = MutableStateFlow(true)
    val rangeState = MutableStateFlow(true)
    val moreThanState = MutableStateFlow(true)
    private var moreThanValueNumeric: Int = -1

    val lifelineResultValue = MutableStateFlow("")
    private var saveGameData = SaveGameData(-1, 0, 0, -1.0, 25)

    var questionCount: Int = -1

    private var questionSetId: Int = -1

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

    private val _bonusPrizesCollected = MutableStateFlow<MutableList<String>>(mutableListOf())
    val bonusPrizesCollected = _bonusPrizesCollected.asStateFlow()

    private val bonusPrizes = MutableStateFlow<MutableList<Prize>>(mutableListOf())

    val bonusPrizeLadderMap = mutableMapOf<Int, Prize>()
    private suspend fun placeBonusPrizesOnLadder() {
        bonusPrizeLadderMap.clear()
        bonusPrizes.value.clear()
        _bonusPrizesCollected.value.clear()
        bonusPrizes.value = repository.getAllPrizesOnce().toMutableList()
        for (i in 0 until bonusPrizes.value.size) {
            val prize = bonusPrizes.value[i]
            if (prize.isCollected) {
                _bonusPrizesCollected.value.add(prize.prizeTitle)
            }
            if (prize.barPosition != null) {
                bonusPrizeLadderMap[prize.barPosition!!] = prize
            }
            else {
                val range = IntRange(i + (i * 20), ((i+1) * 20) - 1) /* spread them out
                between the money checkpoints
                (20 bars between each checkpoint) */
                val position = range.random()
                bonusPrizeLadderMap[position] = prize
                repository.updatePrize(prize = prize.copy(barPosition = position))
            }
        }
    }

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

    fun getNextQuestion() {
        questionCount++
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
        lifelineResultValue.value = ""
    }

    fun loadGame(setId: Int) {
        colorBarStates.addAll(getInitialLadderBars())
        questionSetId = setId
        questionCount = 0
        lifelineResultValue.value = ""
        viewModelScope.launch(Dispatchers.Main) {
            loadSaveGameData()
            placeBonusPrizesOnLadder()
            getQuestion()
        }
    }

    private suspend fun getQuestion() {
        repository.getAllQuestionsInSet(questionSetId).collect { questionsList ->
            _question.value = questionsList[questionCount]
        }
    }
    private suspend fun loadSaveGameData() {
        saveGameData = repository.getSaveGameDataInSet(questionSetId)
        _totalStepsClimbed.value = saveGameData.stepsClimbed
        for (i in 0 until _totalStepsClimbed.value) {
            setColorBarState(i, ColorBarState.PLAYER_INPUT_GRAY,  increment = false)
        }
        questionCount = saveGameData.questionIndex
        _lives.value = saveGameData.lives
        _money.value = saveGameData.money
        moneyTooltip.value = "£%.2f".format(_money.value)
        rangeState.value = saveGameData.rangeUsed
        oddOrEvenState.value = saveGameData.oddOrEvenUsed
        moreThanState.value = saveGameData.moreThanUsed

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

    }

    // set lives at the beginning
    fun setLives(startLives: Int) {
        _lives.value = startLives
    }

    fun onFinishQuestion() {
        viewModelScope.launch {
            repository.updateSaveGameData(saveGameData = saveGameData.copy(stepsClimbed = _totalStepsClimbed.value, lives = _lives.value, money = _money.value, questionIndex = questionCount + 1,
                moreThanUsed = moreThanState.value, oddOrEvenUsed = oddOrEvenState.value, rangeUsed = rangeState.value))
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            repository.resetSaveGameData(questionSetId)
        }
        _bonusPrizesCollected.value.clear()
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

    fun collectBonusPrizeIfPresent(colorBarPosition: Int) {
        if (bonusPrizeLadderMap.containsKey(colorBarPosition)) {
            val prize = bonusPrizeLadderMap[colorBarPosition]!!
            _bonusPrizesCollected.value.add(prize.prizeTitle)
            viewModelScope.launch {
                repository.updatePrize(prize = prize.copy(isCollected = true))
            }
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

    fun useRangeLifeline() {
        val correctAnswer = _question.value.correctAnswer
        val digitsBelow = (0..correctAnswer).random()
        val max = correctAnswer + digitsBelow
        val range = IntRange(digitsBelow, max)
        lifelineResultValue.value = "RANGE: ${range.first} to ${range.last}"
        rangeState.value = false
    }

    fun useMoreThan() {
        val correctAnswer = _question.value.correctAnswer
        if (moreThanValueNumeric <= 0) {
            lifelineResultValue.value = "Please enter a digit of 1 or more"
        }
        else
        {
            if (correctAnswer > moreThanValueNumeric) {
                lifelineResultValue.value = "The answer is MORE than $moreThanValueNumeric"
            }
            else {
                lifelineResultValue.value = "The answer is LESS THAN OR EQUAL TO $moreThanValueNumeric"
            }
            moreThanState.value = false
        }

    }

    fun setMoreThanLifelineValue(value: String) {
        try {
            if (value.isDigitsOnly())
            {
                moreThanValueNumeric = value.trimStart{it == '0'}.toInt()
            }
        }
        catch(e: Exception) {
            // do not store the new value as an integer, just do nothing
        }
    }

    fun useOddOrEvenLifeline() {
        val correctAnswer = _question.value.correctAnswer
        if (correctAnswer.mod(2) == 0) {
            lifelineResultValue.value = "The answer is an EVEN number"
        }
        else
        {
            lifelineResultValue.value = "The answer is an ODD number"
        }
        oddOrEvenState.value = false
    }
}
