package danielle.projects.gameshowspinoff.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import danielle.projects.gameshowspinoff.model.PrizeGameData


@Dao
interface PrizeGameDataDatabaseDao {


    @Query("SELECT * FROM prize_game_data_table WHERE game_id =:gameId AND prize_id =:prizeId")
    suspend fun getPrizeByGameId(gameId: Int, prizeId: Int): PrizeGameData

    @Query("SELECT  * FROM prize_game_data_table WHERE game_id =:gameId")
    fun getAllPrizeDataByGameId(gameId: Int): List<PrizeGameData>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(prizeGameData: PrizeGameData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(prizeGameData: PrizeGameData)

    @Query("UPDATE prize_game_data_table SET is_collected = 0, bar_position = null WHERE game_id =:gameId")
    suspend fun resetAllPrizesByGameId(gameId: Int)

    @Query("DELETE FROM prize_game_data_table")
    suspend fun deleteAll()

    @Query("DELETE FROM prize_game_data_table WHERE game_id=:gameId")
    suspend fun deleteAllPrizeDataByGameId(gameId: Int)
    @Delete
    suspend fun deletePrizeGameData(prizeGameData: PrizeGameData)
}