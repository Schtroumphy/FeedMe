package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import kotlinx.coroutines.flow.Flow

class GetCommandByIdUseCase(
    private val repository: CommandRepository
) {

    operator fun invoke(id: Long): Command? {
        return repository.getCommandById(id)
    }
}