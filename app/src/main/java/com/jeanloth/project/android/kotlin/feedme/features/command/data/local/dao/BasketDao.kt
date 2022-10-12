package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketWithWrappers
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {
    @Query("SELECT * FROM basket")
    fun all() : List<BasketEntity>

    @Insert
    fun insert(basket: BasketEntity) : Long

    @Insert
    fun insertAll(baskets: List<BasketEntity>) : Array<Long>

    @Query("SELECT * FROM basket")
    fun observeAll(): Flow<List<BasketEntity>>

    @Delete
    fun delete(basket: BasketEntity)

    @Transaction
    @Query("SELECT * FROM basket")
    fun observeBasketsWithWrappers(): Flow<List<BasketWithWrappers>>
}