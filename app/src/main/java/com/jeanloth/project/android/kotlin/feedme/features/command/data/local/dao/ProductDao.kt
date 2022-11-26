package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun all() : List<ProductEntity>

    @Query("SELECT * FROM product WHERE id=:id ")
    fun getById(id : Long) : ProductEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM product")
    fun observeAll(): Flow<List<ProductEntity>>

    @Delete
    fun delete(product: ProductEntity)
}