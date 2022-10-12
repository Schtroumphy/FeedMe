package com.jeanloth.project.android.kotlin.feedme.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.AppClientDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.BasketDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.WrapperDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.WrapperEntity

@Database(
    entities = [AppClientEntity::class, ProductEntity::class, BasketEntity::class, WrapperEntity::class],  // Add all new entity to the list there
    version = 1
)
abstract class AppRoomDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "feedme-db"
    }

    // Define all DAOs like this
    abstract fun appClientDao(): AppClientDao
    abstract fun productDao(): ProductDao
    abstract fun basketDao(): BasketDao
    abstract fun wrapperDao(): WrapperDao
}