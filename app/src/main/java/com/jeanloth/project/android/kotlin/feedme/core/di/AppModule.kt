package com.jeanloth.project.android.kotlin.feedme.core.di

import android.app.Application
import androidx.room.Room
import com.jeanloth.project.android.kotlin.feedme.core.database.AppRoomDatabase
import com.jeanloth.project.android.kotlin.feedme.core.database.AppRoomDatabase.Companion.DATABASE_NAME
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.AppClientDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.GetAllClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.RemoveClientUseCase
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

    /** Mappers **/
    @Provides
    fun provideAppClientMapper(): AppClientEntityMapper = AppClientEntityMapper()

    /** DAOs **/
    @Provides
    fun provideAppClientDao(appDatabase: AppRoomDatabase): AppClientDao = appDatabase.appClientDao()


    /** Use cases **/
    @Provides
    @Singleton
    fun provideSaveClient(repository: AppClientRepository) : SaveClientUseCase = SaveClientUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllClient(repository: AppClientRepository) : GetAllClientUseCase = GetAllClientUseCase(repository)

    @Provides
    @Singleton
    fun provideRemoveClient(repository: AppClientRepository) : RemoveClientUseCase = RemoveClientUseCase(repository)

}