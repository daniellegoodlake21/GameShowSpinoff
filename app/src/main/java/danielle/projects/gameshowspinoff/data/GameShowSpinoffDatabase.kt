package danielle.projects.gameshowspinoff.data

import androidx.room.Database
import androidx.room.RoomDatabase
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.model.QuestionSet


@Database(entities = [Question::class, QuestionSet::class], version = 1, exportSchema = false)
abstract class GameShowSpinoffDatabase: RoomDatabase() {

    abstract fun questionDao(): QuestionDatabaseDao

    abstract fun questionSetDao(): QuestionSetDatabaseDao

}