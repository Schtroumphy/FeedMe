package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao

import androidx.room.*
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.CommandEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.CommandWithWrappers
import kotlinx.coroutines.flow.Flow

@Dao
interface CommandDao {
    @Query("SELECT * FROM command")
    fun all() : List<CommandEntity>

    @Update
    fun update(command: CommandEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(command: CommandEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

    @Transaction
    @Query("SELECT * FROM command WHERE id=:id")
    fun observeCommandsWithWrappersById(id : Long): Flow<CommandWithWrappers?>

}