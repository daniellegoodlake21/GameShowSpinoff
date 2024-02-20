package danielle.projects.gameshowspinoff.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import danielle.projects.gameshowspinoff.data.GameShowSpinoffDatabase
import danielle.projects.gameshowspinoff.data.PrizeDatabaseDao
import danielle.projects.gameshowspinoff.data.QuestionDatabaseDao
import danielle.projects.gameshowspinoff.data.QuestionSetDatabaseDao
import danielle.projects.gameshowspinoff.data.SaveGameDataDatabaseDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideSaveGameDataDao(gameDatabase: GameShowSpinoffDatabase): SaveGameDataDatabaseDao = gameDatabase.saveGameDataDao()

    @Singleton
    @Provides
    fun provideQuestionDao(gameDatabase: GameShowSpinoffDatabase): QuestionDatabaseDao
            = gameDatabase.questionDao()

    @Singleton
    @Provides
    fun provideQuestionSetDao(gameDatabase: GameShowSpinoffDatabase): QuestionSetDatabaseDao
            = gameDatabase.questionSetDao()

    @Singleton
    @Provides
    fun providePrizeDao(gameDatabase: GameShowSpinoffDatabase): PrizeDatabaseDao
            = gameDatabase.prizeDao()
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): GameShowSpinoffDatabase
            = Room.databaseBuilder(context,
        GameShowSpinoffDatabase::class.java,
        "game_show_spinoff_database")
        .fallbackToDestructiveMigration()
        .build()
}