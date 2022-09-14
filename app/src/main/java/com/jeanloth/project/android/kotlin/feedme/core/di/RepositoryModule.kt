package com.jeanloth.project.android.kotlin.feedme.core.di

import com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl.AppClientRepositoryImpl
import com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl.ProductRepositoryImpl
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) /// Decide how long the dependency will live
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindClientRepository(impl : AppClientRepositoryImpl) : AppClientRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl : ProductRepositoryImpl) : ProductRepository
}