package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import kotlinx.coroutines.flow.Flow

interface CommandRepository {

    fun save(command : Command) : Long

    fun observeCommands() : Flow<List<Command>>

    fun remove(command : Command)

}