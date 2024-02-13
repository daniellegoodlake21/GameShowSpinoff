package danielle.projects.gameshowspinoff.repository

import danielle.projects.gameshowspinoff.data.QuestionDatabaseDao
import danielle.projects.gameshowspinoff.data.QuestionSetDatabaseDao
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.model.QuestionSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val questionDatabaseDao: QuestionDatabaseDao, private val questionSetDatabaseDao: QuestionSetDatabaseDao) {


    fun getAllQuestionsInSet(setId: Int): Flow<List<Question>> = questionDatabaseDao.getQuestionsBySetId(setId).flowOn(Dispatchers.IO)
        .conflate()

    fun getQuestionCountInSet(setId: Int): Flow<Int> = questionDatabaseDao.getQuestionCountBySetId(setId)

    suspend fun addQuestion(question: Question) = questionDatabaseDao.insert(question)

    suspend fun updateQuestion(question: Question) = questionDatabaseDao.update(question)

    suspend fun deleteQuestion(question: Question) = questionDatabaseDao.deleteQuestion(question)

    suspend fun addQuestionSet(questionSet: QuestionSet) = questionSetDatabaseDao.insert(questionSet)
    suspend fun deleteAllQuestionsInSet(setId: Int) = questionDatabaseDao.deleteAllQuestionsBySetId(setId)

    fun getAllQuestionSets(): Flow<List<QuestionSet>> = questionSetDatabaseDao.getAllQuestionSets()

    suspend fun getQuestionSetById(setId: Int): QuestionSet = questionSetDatabaseDao.getQuestionSetById(setId)

    suspend fun updateQuestionSet(questionSet: QuestionSet) = questionSetDatabaseDao.update(questionSet)

    suspend fun deleteAllQuestionSets() = questionSetDatabaseDao.deleteAll()

    suspend fun deleteQuestionSetById(questionSet: QuestionSet) = questionSetDatabaseDao.deleteQuestionSet(questionSet)
}