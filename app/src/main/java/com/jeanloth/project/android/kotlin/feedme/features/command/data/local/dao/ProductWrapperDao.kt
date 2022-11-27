package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.ProductWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductWrapperDao {
    @Query("SELECT * FROM product_wrapper")
    fun all() : List<ProductWrapperEntity>

    @Update
    fun update(wrappers : List<ProductWrapperEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductWrapperEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(products: List<ProductWrapperEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductWrapperEntity>) : Array<Long>

    @Query("SELECT * FROM product_wrapper")
    fun observeAll(): Flow<List<ProductWrapperEntity>>

    @Delete
    fun delete(wrapper: ProductWrapperEntity)

    @Transaction
    @Query("SELECT * FROM product_wrapper")
    fun observeProdctWrappers(): Flow<List<ProductWrapper>>
}