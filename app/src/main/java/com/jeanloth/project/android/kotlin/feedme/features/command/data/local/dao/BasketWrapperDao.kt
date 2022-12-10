package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.PopulatedBasketWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.PopulatedCommandBasketWrapper
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketWrapperDao {
    @Query("SELECT * FROM basket_wrapper")
    fun all() : List<BasketWrapperEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: BasketWrapperEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<BasketWrapperEntity>) : Array<Long>

    @Query("SELECT * FROM basket_wrapper WHERE commandId=:commandId ")
    fun observeByCommandId(commandId : Long) : Flow<List<PopulatedCommandBasketWrapper>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(products: List<BasketWrapperEntity>)

    @Query("SELECT * FROM basket_wrapper")
    fun observeAll(): Flow<List<BasketWrapperEntity>>

    @Delete
    fun delete(wrapper: BasketWrapperEntity)
}