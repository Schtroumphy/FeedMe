package com.jeanloth.project.android.kotlin.feedme.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.*

@Database(
    entities = [AppClientEntity::class, ProductEntity::class, BasketEntity::class, ProductWrapperEntity::class, BasketWrapperEntity::class, CommandEntity::class],  // Add all new entity to the list there
    version = 2
)
@TypeConverters(DateTypeConverter::class, StatusConverter::class, CommandStatusConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "feedme-db"
    }

    // Define all DAOs like this
    abstract fun appClientDao(): AppClientDao
    abstract fun productDao(): ProductDao
    abstract fun basketDao(): BasketDao
    abstract fun commandDao(): CommandDao

    abstract fun productWrapperDao(): ProductWrapperDao
    abstract fun basketWrapperDao(): BasketWrapperDao
}