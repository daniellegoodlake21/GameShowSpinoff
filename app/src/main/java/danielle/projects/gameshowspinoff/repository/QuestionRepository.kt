package danielle.projects.gameshowspinoff.repository

import danielle.projects.gameshowspinoff.data.PrizeDatabaseDao
import danielle.projects.gameshowspinoff.data.PrizeGameDataDatabaseDao
import danielle.projects.gameshowspinoff.data.QuestionDatabaseDao
import danielle.projects.gameshowspinoff.data.QuestionSetDatabaseDao
import danielle.projects.gameshowspinoff.data.SaveGameDataDatabaseDao
import danielle.projects.gameshowspinoff.model.Prize
import danielle.projects.gameshowspinoff.model.PrizeGameData
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.model.QuestionSet
import danielle.projects.gameshowspinoff.model.SaveGameData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class QuestionRepository @Inject constructor(private val saveGameDataDatabaseDao: SaveGameDataDatabaseDao, private val questionDatabaseDao: QuestionDatabaseDao,
                                             private val questionSetDatabaseDao: QuestionSetDatabaseDao, private val prizeDatabaseDao: PrizeDatabaseDao,
                                             private val prizeGameDataDatabaseDao: PrizeGameDataDatabaseDao) {

    // prize related save game data
    suspend fun getPrizeInSaveGame(saveGameDataId: Int, prizeId: Int): PrizeGameData = prizeGameDataDatabaseDao.getPrizeByGameId(saveGameDataId, prizeId)

    suspend fun insertPrizeGameData(prizeGameData: PrizeGameData) = prizeGameDataDatabaseDao.insert(prizeGameData)

    fun getAllPrizesInSaveGame(saveGameDataId: Int): List<PrizeGameData> = prizeGameDataDatabaseDao.getAllPrizeDataByGameId(saveGameDataId)

    suspend fun updatePrizeDataInSaveGame(prizeGameData: PrizeGameData) = prizeGameDataDatabaseDao.update(prizeGameData)

    suspend fun resetAllPrizesInSaveGame(saveGameDataId: Int) = prizeGameDataDatabaseDao.deleteAllPrizeDataByGameId(saveGameDataId)

    // basic game save data
    suspend fun getSaveGameDataInSet(setId: Int): SaveGameData = saveGameDataDatabaseDao.getSaveGameDataByQuestionSetId(setId)

    suspend fun updateSaveGameData(saveGameData: SaveGameData) = saveGameDataDatabaseDao.update(saveGameData)

    suspend fun deleteSaveGameData(saveGameData: SaveGameData) = saveGameDataDatabaseDao.deleteSaveGameData(saveGameData)


    suspend fun addSaveGameData(saveGameData: SaveGameData): Int = saveGameDataDatabaseDao.insert(saveGameData).toInt()

    suspend fun resetSaveGameData(questionSetId: Int) = saveGameDataDatabaseDao.resetGameByQuestionSetId(questionSetId)

    // questions and question sets
    fun getAllQuestionsInSet(setId: Int): Flow<List<Question>> = questionDatabaseDao.getQuestionsBySetId(setId)

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

    // prizes

    fun getAllPrizes(): Flow<List<Prize>> = prizeDatabaseDao.getAllPrizes()

    suspend fun getAllPrizesOnce(): List<Prize> = prizeDatabaseDao.getAllPrizesOnce()
    suspend fun getPrizeById(id: Int) = prizeDatabaseDao.getPrizeById(id)

    suspend fun addPrize(prize: Prize) = prizeDatabaseDao.insert(prize)

    suspend fun deletePrize(prize: Prize) = prizeDatabaseDao.deletePrize(prize)

    suspend fun updatePrize(prize: Prize) = prizeDatabaseDao.update(prize)

}