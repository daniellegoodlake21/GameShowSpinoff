package danielle.projects.gameshowspinoff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import danielle.projects.gameshowspinoff.model.Question
import kotlinx.coroutines.flow.Flow


@Dao
interface QuestionDatabaseDao {
    @Query("SELECT  * FROM question_table")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM question_table WHERE id =:id")
    fun getQuestionById(id: Int): Question

    @Query("SELECT * FROM question_table WHERE id =:questionSetId")
    fun getQuestionsBySetId(questionSetId: Int): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(question: Question)

    @Query("DELETE FROM question_table")
    suspend fun deleteAll()

    @Query("DELETE FROM question_table WHERE question_set_id =:questionSetId")
    suspend fun deleteAllQuestionsBySetId(questionSetId: Int)
    @Delete
    suspend fun deleteQuestion(note: Question)
}
