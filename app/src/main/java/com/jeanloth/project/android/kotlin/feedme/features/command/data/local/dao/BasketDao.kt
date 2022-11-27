package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.PopulatedBasket
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {
    @Query("SELECT * FROM basket")
    fun all() : List<BasketEntity>

    @Query("SELECT * FROM basket WHERE id=:id ")
    fun getById(id : Long) : BasketEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(basket: BasketEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(baskets: List<BasketEntity>) : Array<Long>

    @Query("SELECT * FROM basket")
    fun observeAll(): Flow<List<BasketEntity>>

    @Delete
    fun delete(basket: BasketEntity)

    @Transaction
    @Query("SELECT * FROM basket")
    fun observeBasketsWithWrappers(): Flow<List<PopulatedBasket>>

    @Transaction
    @Query("SELECT * FROM basket")
    fun getBasketsWithWrappers(): List<PopulatedBasket>
}