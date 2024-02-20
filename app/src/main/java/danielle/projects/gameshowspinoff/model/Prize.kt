package danielle.projects.gameshowspinoff.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prize_table")
data class Prize(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "prize_title")
    var prizeTitle: String,
    @ColumnInfo(name = "is_collected")
    var isCollected: Boolean = false,
    @ColumnInfo(name = "bar_position")
    var barPosition: Int? = null)