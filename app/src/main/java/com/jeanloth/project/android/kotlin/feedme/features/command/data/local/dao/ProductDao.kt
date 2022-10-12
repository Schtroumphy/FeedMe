package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun all() : List<ProductEntity>

    @Insert
    fun insert(product: ProductEntity)

    @Insert
    fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM product")
    fun observeAll(): Flow<List<ProductEntity>>

    @Delete
    fun delete(product: ProductEntity)
}