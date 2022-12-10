package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.CommandBasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.PopulatedBasket
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.PopulatedCommandBasket
import kotlinx.coroutines.flow.Flow

@Dao
interface CommandBasketDao {
    @Query("SELECT * FROM command_basket")
    fun all() : List<CommandBasketEntity>

    @Query("SELECT * FROM command_basket WHERE id=:id ")
    fun getById(id : Long) : CommandBasketEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(basket: CommandBasketEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(baskets: List<CommandBasketEntity>) : Array<Long>

    @Query("SELECT * FROM command_basket")
    fun observeAll(): Flow<List<CommandBasketEntity>>

    @Delete
    fun delete(basket: CommandBasketEntity)

    @Transaction
    @Query("SELECT * FROM command_basket")
    fun observeCommandBasketsWithWrappers(): Flow<List<PopulatedCommandBasket>>

    @Transaction
    @Query("SELECT * FROM command_basket")
    fun getCommandBasketsWithWrappers(): List<PopulatedCommandBasket>
}