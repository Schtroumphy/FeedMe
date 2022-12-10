package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketRepository
import kotlinx.coroutines.flow.Flow

/**
 * Observe basket created by user to simplify command creation - Not associated to command here
 *
 * Just used to group products wrappers
 */
class ObserveBasketsUseCase(
    private val repository: BasketRepository
) {

    operator fun invoke(): Flow<List<Basket>> {
        return repository.observeBaskets()
    }
}