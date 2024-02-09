package danielle.projects.gameshowspinoff.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "question_table")
data class Question(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "question_text")
    val questionText: String,
    @ColumnInfo(name = "correct_answer")
    val correctAnswer: Int,
    @ColumnInfo(name = "question_set_id")
    val questionsSetId: Int)
