package danielle.projects.gameshowspinoff.data

import androidx.room.Database
import androidx.room.RoomDatabase
import danielle.projects.gameshowspinoff.model.Prize
import danielle.projects.gameshowspinoff.model.Question
import danielle.projects.gameshowspinoff.model.QuestionSet
import danielle.projects.gameshowspinoff.model.SaveGameData


@Database(entities = [Question::class, QuestionSet::class, Prize::class, SaveGameData::class], version = 5, exportSchema = false)
abstract class GameShowSpinoffDatabase: RoomDatabase() {

    abstract fun questionDao(): QuestionDatabaseDao

    abstract fun questionSetDao(): QuestionSetDatabaseDao

    abstract fun prizeDao(): PrizeDatabaseDao

    abstract fun saveGameDataDao(): SaveGameDataDatabaseDao

}