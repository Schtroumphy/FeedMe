package com.jeanloth.project.android.kotlin.feedme.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jeanloth.project.android.kotlin.feedme.data.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.feedme.data.local.dao.AppClientDao

@Database(
    entities = [AppClientEntity::class],  // Add all new entity to the list there
    version = 1
)
abstract class AppRoomDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "feedme-db"
    }

    // Dafine all daos like this
    abstract fun appClientDao(): AppClientDao
}