package danielle.projects.gameshowspinoff.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.model.QuestionSet
import danielle.projects.gameshowspinoff.repository.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionBuilderViewModel @Inject constructor(private val repository: QuestionRepository): ViewModel() {

    val questionSetList = MutableStateFlow<List<QuestionSet>>(mutableListOf())

    val currentQuestionList = MutableStateFlow<List<Question>>(mutableListOf())

    val questionCountInQuestionSetMap = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllQuestionSets().distinctUntilChanged().collect{
                    listOfQuestionSets ->
                questionSetList.value = listOfQuestionSets
            }
        }
    }

    fun getQuestionsListByQuestionSet(questionSetId: Int) {
        viewModelScope.launch {
            repository.getAllQuestionsInSet(questionSetId).distinctUntilChanged().collect{ value ->
                currentQuestionList.value = value
            }
        }
    }

    fun getQuestionCountInQuestionSet(questionSet: QuestionSet) {
        viewModelScope.launch {
            repository.getQuestionCountInSet(questionSet.id).distinctUntilChanged()
                .collect { count ->
                    questionCountInQuestionSetMap.value[questionSet.id] = count
                }
        }
    }
    fun addQuestion(question: Question) {
        viewModelScope.launch {
            repository.addQuestion(question = question)
        }
    }

    fun removeQuestion(question: Question) {
        viewModelScope.launch {
            repository.deleteQuestion(question = question)
        }
    }

    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            repository.updateQuestion(question = question)
        }
    }

    fun updateQuestionSet(questionSet: QuestionSet) {
        viewModelScope.launch {
            repository.updateQuestionSet(questionSet = questionSet)
        }
    }
    fun addQuestionSet(questionSet: QuestionSet) {
        viewModelScope.launch {
            repository.addQuestionSet(questionSet = questionSet)
        }
    }
}