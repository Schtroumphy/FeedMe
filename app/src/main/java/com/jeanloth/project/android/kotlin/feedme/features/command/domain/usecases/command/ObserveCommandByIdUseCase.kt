package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import kotlinx.coroutines.flow.Flow

class ObserveCommandByIdUseCase(
    private val repository: CommandRepository
) {

    operator fun invoke(commandId : Long): Flow<Command?> {
        return repository.observeCommandById(commandId)

    }

}