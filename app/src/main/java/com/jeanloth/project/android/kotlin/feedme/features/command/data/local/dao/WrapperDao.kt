package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.WrapperEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WrapperDao {
    @Query("SELECT * FROM wrapper")
    fun all() : List<WrapperEntity>

    @Insert
    fun insert(product: WrapperEntity) : Long

    @Insert
    fun insertAll(products: List<WrapperEntity>) : Array<Long>

    @Query("SELECT * FROM wrapper")
    fun observeAll(): Flow<List<WrapperEntity>>

    @Delete
    fun delete(wrapper: WrapperEntity)

    @Transaction
    @Query("SELECT * FROM wrapper")
    fun getDogsAndOwners(): List<ProductWrapper>
}