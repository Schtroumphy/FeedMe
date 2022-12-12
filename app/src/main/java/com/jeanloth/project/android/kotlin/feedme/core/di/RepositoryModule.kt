package com.jeanloth.project.android.kotlin.feedme.core.di

import com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveBasketUseCase
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

    @Binds
    @Singleton
    abstract fun bindBasketRepository(impl : BasketRepositoryImpl) : BasketRepository

    @Binds
    @Singleton
    abstract fun bindCommandRepository(impl : CommandRepositoryImpl) : CommandRepository

    /** Wrappers **/
    @Binds
    @Singleton
    abstract fun bindWrapperProductRepository(impl : WrapperProductRepositoryImpl) : ProductWrapperRepository

    @Binds
    @Singleton
    abstract fun bindWrapperBasketRepository(impl : WrapperBasketRepositoryImpl) : BasketWrapperRepository

    @Binds
    @Singleton
    abstract fun bindGoogleMapRepository(impl : GoogleMapRepositoryImpl) : GoogleMapRepository

}