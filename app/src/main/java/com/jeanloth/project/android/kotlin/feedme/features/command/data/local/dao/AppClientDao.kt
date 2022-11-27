package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.AppClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppClientDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(client: AppClientEntity)

    @Query("SELECT * FROM app_client WHERE idClient=:id ")
    fun getById(id : Long) : AppClientEntity

    @Query("SELECT * FROM app_client")
    fun observeAll(): Flow<List<AppClientEntity>>

    @Delete
    fun delete(client: AppClientEntity)
}
