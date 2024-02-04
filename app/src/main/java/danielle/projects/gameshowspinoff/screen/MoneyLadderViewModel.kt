package danielle.projects.gameshowspinoff.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoneyLadderViewModel @Inject constructor(): ViewModel() {

    private val _totalStepsClimbed = MutableStateFlow(0)
    val totalStepsClimbed = _totalStepsClimbed.asStateFlow()

    private val _money = MutableStateFlow(0.0f) // in £ GBP
    val money = _money.asStateFlow()

    private val _lives = MutableStateFlow(0)
    val lives = _lives.asStateFlow()

    private var lostGame = false

    // map of which bars being reached or surpassed give which money on receipt of an exact answer
    private val moneyCheckpoints: Map<Int, Float> = mapOf(10 to 1f, 20 to 1.5f, 30 to 2f, 40 to 4f, 50 to 8f, 60 to 12.5f, 70 to 20f, 80 to 30f, 90 to 45f, 100 to 60f)

    // bank the money when an exact answer is given
    private fun bankMoney(moneyToBank: Float) {
        _money.value = moneyToBank
    }

    private fun loseGame() {
        lostGame = true
        _money.value /= 2 // in this version of the game, you still win half the money you have banked even if you lose
    }

    /* set the total steps climbed: the result is either
    if still in the game and exact answer:
        - the number of user steps (which in this case will be the same as the correct number)
    or if still in the game but losing lives:
        - the correct number of steps
    or  if losing the game due to an answer too high
        - the correct number of steps plus one
    or if losing the game due to losing too many lives
        - the number of lives left minus the difference between the current step and the correct number of steps */
    fun moveUpLadder(userStepsToClimb: Int, correctNumberOfSteps: Int){
        val exactAnswer = userStepsToClimb == correctNumberOfSteps
        var step = 0
        var moneyCheckpointPassed: Int? = null
        while (step < userStepsToClimb) {
            step++
            viewModelScope.launch {
                delay(500) // delay for half a second after each step
            }
            // check if money checkpoint is passed
            if (moneyCheckpoints.containsKey(step + _totalStepsClimbed.value))
            {
                moneyCheckpointPassed = step
            }
            // correctNumberOfSteps is simply the numerical answer to the current question (it is NOT the total overall)
            if (step > correctNumberOfSteps || _lives.value - correctNumberOfSteps - _totalStepsClimbed.value < 0)
            {
                loseGame()
                break
            }
        }
        // if losing lives but still in the game
        if (!lostGame) {
            if (exactAnswer) {
                // get highest money-banking bar went through in the answer as moving up the ladder
                if (moneyCheckpointPassed != null) {
                    // definitely not null as checked against beforehand, without changing any values
                    val moneyToBank = moneyCheckpoints[moneyCheckpointPassed]!!
                    bankMoney(moneyToBank = moneyToBank)
                }
            }
            else {
                // lose lives
                var remainingExtraSteps = correctNumberOfSteps - userStepsToClimb
                while (remainingExtraSteps > 0) {
                    viewModelScope.launch {
                        delay(1000) // delay for 1 second after each life lost
                    }
                    _totalStepsClimbed.value++
                    remainingExtraSteps--
                }
            }
        }
        _totalStepsClimbed.value += step
    }

}
