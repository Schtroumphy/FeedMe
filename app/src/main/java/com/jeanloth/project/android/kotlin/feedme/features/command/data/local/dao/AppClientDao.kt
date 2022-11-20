package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppClientDao{

    @Insert
    fun insert(client: AppClientEntity)

    @Query("SELECT * FROM app_client WHERE idClient=:id ")
    fun getById(id : Long) : AppClientEntity

    @Query("SELECT * FROM app_client")
    fun observeAll(): Flow<List<AppClientEntity>>

    @Delete
    fun delete(client: AppClientEntity)
}
