package danielle.projects.gameshowspinoff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.model.QuestionSet
import kotlinx.coroutines.flow.Flow


@Dao
interface QuestionSetDatabaseDao {

    @Query("SELECT  * FROM question_set_table")
    fun getAllQuestionSets(): Flow<List<QuestionSet>>

    @Query("SELECT * FROM question_set_table WHERE id =:id")
    suspend fun getQuestionSetById(id: Int): QuestionSet

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questionSet: QuestionSet)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(questionSet: QuestionSet)

    @Query("DELETE FROM question_set_table")
    suspend fun deleteAll()
    @Delete
    suspend fun deleteQuestionSet(questionSet: Question)
}