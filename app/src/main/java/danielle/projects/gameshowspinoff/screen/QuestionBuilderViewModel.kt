package danielle.projects.gameshowspinoff.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.model.QuestionSet
import danielle.projects.gameshowspinoff.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionBuilderViewModel @Inject constructor(private val repository: QuestionRepository): ViewModel() {

    private val _questionSetList = MutableStateFlow<List<QuestionSet>>(mutableListOf())
    val questionSetList = _questionSetList.asStateFlow()

    private val _currentQuestionList = MutableStateFlow<List<Question>>(mutableListOf())
    val currentQuestionList = _currentQuestionList.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllQuestionSets().distinctUntilChanged().collect{ value ->
                _questionSetList.value = value
            }
        }
    }

    fun getQuestionsListByQuestionSet(questionSet: QuestionSet) {
        viewModelScope.launch {
            repository.getAllQuestionsInSet(questionSet.id).distinctUntilChanged().collect{ value ->
                _currentQuestionList.value = value
            }
        }
    }

    fun getQuestionCountInQuestionSet(questionSet: QuestionSet): Int {
        getQuestionsListByQuestionSet(questionSet = questionSet)
        return _currentQuestionList.value.size
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