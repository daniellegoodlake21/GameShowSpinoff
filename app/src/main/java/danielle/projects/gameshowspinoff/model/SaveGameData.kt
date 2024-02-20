package danielle.projects.gameshowspinoff.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "save_game_data_table")
data class SaveGameData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "steps_climbed")
    var stepsClimbed: Int = 0,
    @ColumnInfo(name = "question_index")
    var questionIndex: Int = 0,
    var money: Double = 0.0,
    var lives: Int = 25,
    @ColumnInfo(name = "range_used")
    val rangeUsed: Boolean = true,
    @ColumnInfo(name = "odd_or_even_used")
    val oddOrEvenUsed: Boolean = true,
    @ColumnInfo(name = "more_than_used")
    val moreThanUsed: Boolean = true)
