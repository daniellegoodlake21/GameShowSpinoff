package danielle.projects.gameshowspinoff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import danielle.projects.gameshowspinoff.model.Prize
import kotlinx.coroutines.flow.Flow


@Dao
interface PrizeDatabaseDao {

    @Query("SELECT  * FROM prize_table")
    fun getAllPrizes(): Flow<List<Prize>>

    @Query("SELECT * FROM prize_table WHERE id =:id")
    suspend fun getPrizeById(id: Int): Prize

    @Query("SELECT  * FROM prize_table")
    suspend fun getAllPrizesOnce(): List<Prize>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prize: Prize): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(prize: Prize)

    @Query("DELETE FROM prize_table")
    suspend fun deleteAll()
    @Delete
    suspend fun deletePrize(prize: Prize)
}