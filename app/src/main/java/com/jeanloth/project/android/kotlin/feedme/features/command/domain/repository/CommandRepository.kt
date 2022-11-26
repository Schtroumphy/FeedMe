package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import kotlinx.coroutines.flow.Flow

interface CommandRepository {

    fun save(command : Command) : Long

    fun getCommandById(id: Long) : Command?

    fun observeCommandById(id: Long) : Flow<Command?>

    fun observeCommands() : Flow<List<Command>>

    fun remove(command : Command)

    fun update(command: Command)
}