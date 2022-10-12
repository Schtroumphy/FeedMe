package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllBasketsUseCase(
    private val repository: BasketRepository
) {

    operator fun invoke(): Flow<List<Basket>> {
        return repository.observeBaskets()
    }
}