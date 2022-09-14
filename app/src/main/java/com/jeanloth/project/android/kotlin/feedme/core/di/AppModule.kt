package com.jeanloth.project.android.kotlin.feedme.core.di

import android.app.Application
import androidx.room.Room
import com.jeanloth.project.android.kotlin.feedme.core.database.AppRoomDatabase
import com.jeanloth.project.android.kotlin.feedme.core.database.AppRoomDatabase.Companion.DATABASE_NAME
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.AppClientDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.ProductEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.GetAllClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.RemoveClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.SaveClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.ObserveAllProductsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.SaveProductUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.SyncProductUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /** Mappers **/
    @Provides
    fun provideAppClientMapper(): AppClientEntityMapper = AppClientEntityMapper()

    @Provides
    fun provideProductMapper(): ProductEntityMapper = ProductEntityMapper()

    /** DAOs **/
    @Provides
    fun provideAppClientDao(appDatabase: AppRoomDatabase): AppClientDao = appDatabase.appClientDao()

    @Provides
    fun provideProductDao(appDatabase: AppRoomDatabase): ProductDao = appDatabase.productDao()


    /** Use cases **/

    // CLient
    @Provides
    @Singleton
    fun provideSaveClient(repository: AppClientRepository) : SaveClientUseCase = SaveClientUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllClient(repository: AppClientRepository) : GetAllClientUseCase = GetAllClientUseCase(repository)

    @Provides
    @Singleton
    fun provideRemoveClient(repository: AppClientRepository) : RemoveClientUseCase = RemoveClientUseCase(repository)

    // Product
    @Provides
    @Singleton
    fun provideSaveProduct(repository: ProductRepository) : SaveProductUseCase = SaveProductUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllProducts(repository: ProductRepository) : ObserveAllProductsUseCase = ObserveAllProductsUseCase(repository)

    @Provides
    @Singleton
    fun provideSyncProducts(repository: ProductRepository) : SyncProductUseCase = SyncProductUseCase(repository)

    //@Provides
    //@Singleton
    //fun provideRemoveProduct(repository: ProductRepository) : RemoveProductUseCase = RemoveProductUseCase(repository)

}