package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketWithWrappers
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.CommandEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.CommandWithWrappers
import kotlinx.coroutines.flow.Flow

@Dao
interface CommandDao {
    @Query("SELECT * FROM command")
    fun all() : List<CommandEntity>

    @Insert
    fun insert(command: CommandEntity) : Long

    @Insert
    fun insertAll(commands: List<CommandEntity>) : Array<Long>

    @Query("SELECT * FROM command")
    fun observeAll(): Flow<List<CommandEntity>>

    @Delete
    fun delete(command: CommandEntity)

    @Transaction
    @Query("SELECT * FROM command")
    fun observeCommandsWithWrappers(): Flow<List<CommandWithWrappers>>

    @Transaction
    @Query("SELECT * FROM command WHERE id=:id")
    fun getCommandsWithWrappersById(id : Long): CommandWithWrappers?

}