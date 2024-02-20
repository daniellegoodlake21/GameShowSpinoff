package danielle.projects.gameshowspinoff.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import danielle.projects.gameshowspinoff.model.Prize
import danielle.projects.gameshowspinoff.repository.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrizeBuilderViewModel @Inject constructor(private val repository: QuestionRepository): ViewModel() {

    val prizeList = MutableStateFlow<List<Prize>>(mutableListOf())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPrizes().distinctUntilChanged().collect{
                    listOfPrizes ->
                prizeList.value = listOfPrizes
            }
        }
    }


    fun addPrize(prize: Prize) {
        viewModelScope.launch {
            repository.addPrize(prize = prize)
        }
    }

    fun removePrize(prize: Prize) {
        viewModelScope.launch {
            repository.deletePrize(prize = prize)
        }
    }

    fun updatePrize(prize : Prize) {
        viewModelScope.launch {
            repository.updatePrize(prize)
        }
    }

}