package com.jeanloth.project.android.kotlin.feedme.core.di

import android.app.Application
import androidx.room.Room
import com.jeanloth.project.android.kotlin.feedme.data.local.db.AppRoomDatabase
import com.jeanloth.project.android.kotlin.feedme.data.local.db.AppRoomDatabase.Companion.DATABASE_NAME
import com.jeanloth.project.android.kotlin.feedme.data.local.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.feedme.data.repository.AppClientRepositoryImpl
import com.jeanloth.project.android.kotlin.feedme.domain.repository.AppClientRepository
import com.jeanloth.project.android.kotlin.feedme.domain.usecases.client.SaveClientUseCase
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

    @Provides
    @Singleton
    fun provideClientEntityMapper() : AppClientEntityMapper{
        return AppClientEntityMapper()
    }

    @Provides
    @Singleton
    fun provideAppClientRepository(db : AppRoomDatabase, mapper: AppClientEntityMapper) : AppClientRepository {
        return AppClientRepositoryImpl(db.appClientDao(), mapper)
    }

    @Provides
    @Singleton
    fun provideSaveClient(repository: AppClientRepository) : SaveClientUseCase {
        return SaveClientUseCase(repository)
    }
}