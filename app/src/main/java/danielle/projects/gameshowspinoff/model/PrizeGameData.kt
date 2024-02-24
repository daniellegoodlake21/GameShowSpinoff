package danielle.projects.gameshowspinoff.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "prize_game_data_table", primaryKeys = ["prize_id", "game_id"])
data class PrizeGameData(
    @ColumnInfo(name = "prize_id")
    val prizeId: Int,
    @ColumnInfo(name = "game_id")
    val gameId: Int,
    @ColumnInfo(name = "is_collected")
    var isCollected: Boolean = false,
    @ColumnInfo(name = "bar_position")
    var barPosition: Int? = null)