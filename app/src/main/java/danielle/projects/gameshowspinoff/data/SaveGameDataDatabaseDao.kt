package danielle.projects.gameshowspinoff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import danielle.projects.gameshowspinoff.model.SaveGameData


@Dao
interface SaveGameDataDatabaseDao {


    @Query("SELECT * FROM save_game_data_table WHERE id in (SELECT question_set_table.save_game_data_id q FROM question_set_table WHERE question_set_table.id =:questionSetId)")
    suspend fun getSaveGameDataByQuestionSetId(questionSetId: Int): SaveGameData

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(saveGameData: SaveGameData): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(saveGameData: SaveGameData)

    @Query("UPDATE save_game_data_table SET range_used = 1, more_than_used = 1, odd_or_even_used = 1, steps_climbed = 0, question_index = 0, lives = 25, money = 0.0 WHERE id in (SELECT question_set_table.save_game_data_id q FROM question_set_table WHERE question_set_table.id =:questionSetId)")
    suspend fun resetGameByQuestionSetId(questionSetId: Int)

    @Query("DELETE FROM save_game_data_table")
    suspend fun deleteAll()
    @Delete
    suspend fun deleteSaveGameData(saveGameData: SaveGameData)
}