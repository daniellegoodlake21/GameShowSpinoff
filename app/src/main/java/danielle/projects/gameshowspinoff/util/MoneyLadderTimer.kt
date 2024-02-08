package danielle.projects.gameshowspinoff.util

import android.os.CountDownTimer
import danielle.projects.gameshowspinoff.screen.MoneyLadderViewModel

class MoneyLadderTimer(moneyLadderViewModel: MoneyLadderViewModel, userNumberOfSteps: Int, correctNumberOfSteps: Int) {

    private var barsClimbed = moneyLadderViewModel.totalStepsClimbed.value
    private var initialPosition = barsClimbed
    private val millisecondsDelayPerBar = 500
    private val livesLost = correctNumberOfSteps - userNumberOfSteps
    private var moneyCheckpointPassed: Int? = null
    var livesLostSoFarCount = 0

    // the first time moving up the ladder is to check the player hasn't gone over only
    val playerMoveTimer = object: CountDownTimer((userNumberOfSteps * millisecondsDelayPerBar).toLong(), millisecondsDelayPerBar.toLong()) {

        override fun onTick(millisecondsUntilFinished: Long) {
            if (moneyLadderViewModel.gameState.value != GameState.PLAYER_MOVE) {
                moneyLadderViewModel.setGameState(GameState.PLAYER_MOVE)
            }
            moneyLadderViewModel.setTooltip("$barsClimbed")

            // if above (even by one)...
            if (initialPosition + correctNumberOfSteps < barsClimbed) {
                moneyLadderViewModel.setColorBarState(barsClimbed, ColorBarState.LOST_LIFE_RED)
                moneyLadderViewModel.loseGame()
                cancel()
            } else {
                moneyLadderViewModel.setColorBarState(barsClimbed, ColorBarState.PLAYER_INPUT_GRAY)
                moneyLadderViewModel.setTooltip("Count ${barsClimbed + 1}")
                if (moneyLadderViewModel.moneyCheckpoints.containsKey(barsClimbed)) {
                    moneyCheckpointPassed = barsClimbed
                }
            }
            barsClimbed++
        }

        override fun onFinish() {
            moneyLadderViewModel.setTooltip("Waiting to Reveal Answer")
            moneyLadderViewModel.setGameState(GameState.WAIT_FOR_PLAYER_TO_ASK_FOR_RESULTS)
        }

    }
        // the second time moving up the ladder shows the number of lives lost

        val livesLostTimer = object: CountDownTimer((livesLost * millisecondsDelayPerBar).toLong(), millisecondsDelayPerBar.toLong()) {

        override fun onTick(millisecondsUntilFinished: Long) {
            if (moneyLadderViewModel.gameState.value != GameState.PLAYER_MOVE_RESULTS) {
                moneyLadderViewModel.setGameState(GameState.PLAYER_MOVE_RESULTS)
            }

                if (livesLostSoFarCount < livesLost) {
                    moneyLadderViewModel.setColorBarState(barsClimbed, ColorBarState.LOST_LIFE_RED)
                    livesLostSoFarCount++
                    moneyLadderViewModel.setTooltip("Lost $livesLostSoFarCount Lives...")
                    if (livesLostSoFarCount > moneyLadderViewModel.lives.value)
                    {
                        moneyLadderViewModel.loseGame()
                        cancel()
                    }
                    barsClimbed++
                }
        }

        override fun onFinish() {
            if (livesLost == 0) {
                // exact answer
                moneyCheckpointPassed?.let {
                    moneyLadderViewModel.bankMoney(moneyCheckpoint = moneyCheckpointPassed!!)
                }
                moneyLadderViewModel.addLives(5)
                moneyLadderViewModel.setTooltip("EXACT ANSWER!!")
                moneyLadderViewModel.setGameState(GameState.WAIT_FOR_FLASH_GOLD_CORRECT)
                moneyLadderViewModel.flashGoldCorrect()
            }
            else
            {
                moneyLadderViewModel.setTooltip("$livesLost Lives Lost")
            }
        }
    }
}
