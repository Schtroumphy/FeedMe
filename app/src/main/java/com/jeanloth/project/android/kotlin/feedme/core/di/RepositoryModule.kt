package com.jeanloth.project.android.kotlin.feedme.core.di

import com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl.AppClientRepositoryImpl
import com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl.BasketRepositoryImpl
import com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl.ProductRepositoryImpl
import com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl.WrapperProductRepositoryImpl
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BaseRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository
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
    abstract fun bindWrapperProductRepository(impl : WrapperProductRepositoryImpl) : BaseRepository<Wrapper<Product>>

    @Binds
    @Singleton
    abstract fun bindBasketRepository(impl : BasketRepositoryImpl) : BasketRepository

}