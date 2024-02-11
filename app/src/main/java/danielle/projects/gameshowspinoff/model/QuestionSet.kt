package danielle.projects.gameshowspinoff.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_set_table")
data class QuestionSet(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "question_set_label")
    var label: String)