package com.jeanloth.project.android.kotlin.feedme.core.di

import android.app.Application
import androidx.room.Room
import com.jeanloth.project.android.kotlin.feedme.core.db.AppRoomDatabase
import com.jeanloth.project.android.kotlin.feedme.core.db.AppRoomDatabase.Companion.DATABASE_NAME
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.SaveClientUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppRoomDatabase(app: Application) : AppRoomDatabase {
        return Room.databaseBuilder(
            app,
            AppRoomDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    /** Use cases **/
    @Provides
    @Singleton
    fun provideSaveClient(repository: AppClientRepository) : SaveClientUseCase {
        return SaveClientUseCase(repository)
    }
}