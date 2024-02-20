package danielle.projects.gameshowspinoff.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_set_table")
data class QuestionSet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "question_set_label")
    var label: String,
    @ColumnInfo(name = "save_game_data_id")
    val saveGameDataId: Int? = null)