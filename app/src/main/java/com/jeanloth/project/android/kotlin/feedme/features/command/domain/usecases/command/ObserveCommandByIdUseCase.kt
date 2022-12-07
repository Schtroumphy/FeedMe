package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketWrapperRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveCommandByIdUseCase(
    private val repository: CommandRepository,
    private val basketRepository : BasketWrapperRepository
) {

    operator fun invoke(commandId : Long): Flow<Command?> {
        return combine(
            repository.observeCommandById(commandId),
            basketRepository.observeBasketWrappersByCommandId(commandId)
        ){ command, basketWrappers ->

            if(basketWrappers.isNotEmpty()){
                command.apply {
                    this?.basketWrappers = basketWrappers
                }
            }
            command
        }
    }

    // If not ok, try to :
    // Combine
    // Observe command by id
    // Observe product wrappers by command id
    // Observe basket wrappers by command id

}