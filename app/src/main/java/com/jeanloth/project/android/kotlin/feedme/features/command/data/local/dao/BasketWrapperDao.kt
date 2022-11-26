package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketWrapperDao {
    @Query("SELECT * FROM basket_wrapper")
    fun all() : List<BasketWrapperEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: BasketWrapperEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<BasketWrapperEntity>) : Array<Long>

    @Query("SELECT * FROM basket_wrapper")
    fun observeAll(): Flow<List<BasketWrapperEntity>>

    @Delete
    fun delete(wrapper: BasketWrapperEntity)
}