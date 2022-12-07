package com.jeanloth.project.android.kotlin.feedme.core.di

import android.app.Application
import androidx.room.Room
import com.jeanloth.project.android.kotlin.feedme.core.database.AppRoomDatabase
import com.jeanloth.project.android.kotlin.feedme.core.database.AppRoomDatabase.Companion.DATABASE_NAME
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.BasketEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.CommandEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.ProductEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.GetAllClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.RemoveClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.SaveClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.*
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

    @Provides
    fun provideBasketEntityMapper(): BasketEntityMapper = BasketEntityMapper()

    @Provides
    fun provideCommandEntityMapper(): CommandEntityMapper = CommandEntityMapper()

    /** --- DAOs --- **/
    @Provides
    fun provideAppClientDao(appDatabase: AppRoomDatabase): AppClientDao = appDatabase.appClientDao()

    @Provides
    fun provideProductDao(appDatabase: AppRoomDatabase): ProductDao = appDatabase.productDao()

    @Provides
    fun provideBasketDao(appDatabase: AppRoomDatabase): BasketDao = appDatabase.basketDao()

    @Provides
    fun provideCommandDao(appDatabase: AppRoomDatabase): CommandDao = appDatabase.commandDao()

    @Provides
    fun provideProductWrapperDao(appDatabase: AppRoomDatabase): ProductWrapperDao = appDatabase.productWrapperDao()

    @Provides
    fun provideBasketWrapperDao(appDatabase: AppRoomDatabase): BasketWrapperDao = appDatabase.basketWrapperDao()

    /** --- Use cases --- **/

    // Client
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
    fun provideObserveAllProducts(repository: ProductRepository) : ObserveAllProductsUseCase = ObserveAllProductsUseCase(repository)

    @Provides
    @Singleton
    fun provideSyncProducts(repository: ProductRepository) : SyncProductUseCase = SyncProductUseCase(repository)

    // COmmand
    @Provides
    @Singleton
    fun provideObserveCommands(repository: CommandRepository) : ObserveAllCommandsUseCase = ObserveAllCommandsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCommandById(repository: CommandRepository) : GetCommandByIdUseCase = GetCommandByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideObserveCommandById(repository: CommandRepository, basketWrapperRepository: BasketWrapperRepository) : ObserveCommandByIdUseCase = ObserveCommandByIdUseCase(repository, basketWrapperRepository)

    /** --- Wrappers --- **/
    @Provides
    @Singleton
    fun provideSaveProductWrapper(repository: ProductWrapperRepository) : SaveProductWrapperUseCase = SaveProductWrapperUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveBasketWrapper(repository: BasketWrapperRepository) : SaveBasketWrapperUseCase = SaveBasketWrapperUseCase(repository)

    /** --- Objects : Basket --- **/
    @Provides
    @Singleton
    fun provideSaveBasket(repository: BasketRepository, saveProductWrapperUseCase: SaveProductWrapperUseCase) : SaveBasketUseCase = SaveBasketUseCase(repository, saveProductWrapperUseCase)

    @Provides
    @Singleton
    fun provideSaveCommand(repository: CommandRepository, saveBasketWrapperUseCase: SaveBasketWrapperUseCase, saveProductWrapperUseCase: SaveProductWrapperUseCase) : SaveCommandUseCase
    = SaveCommandUseCase(repository, saveBasketWrapperUseCase, saveProductWrapperUseCase)

    @Provides
    @Singleton
    fun provideUpdateCommand(repository: CommandRepository, saveBasketWrapperUseCase: SaveBasketWrapperUseCase, updateProductWrapperUseCase: UpdateProductWrapperUseCase) : UpdateCommandUseCase
    = UpdateCommandUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateProductWrappers(repository: ProductWrapperRepository) : UpdateProductWrapperUseCase = UpdateProductWrapperUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateBasketWrapperUseCase(repository: BasketWrapperRepository) : UpdateBasketWrapperUseCase = UpdateBasketWrapperUseCase(repository)

    /** --- Others --- **/
    @Provides
    @Singleton
    fun provideObserveAllBaskets(repository: BasketRepository) : ObserveAllBasketsUseCase = ObserveAllBasketsUseCase(repository)


}