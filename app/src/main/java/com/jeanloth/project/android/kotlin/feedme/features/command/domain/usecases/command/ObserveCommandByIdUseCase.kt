package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductWrapperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveCommandByIdUseCase(
    private val repository: CommandRepository
) {

    operator fun invoke(commandId : Long): Flow<Command?> {
        return repository.observeCommandById(commandId)
    }

    // If not ok, try to :
    // Combine
    // Observe command by id
    // Observe product wrappers by command id
    // Observe basket wrappers by command id

}