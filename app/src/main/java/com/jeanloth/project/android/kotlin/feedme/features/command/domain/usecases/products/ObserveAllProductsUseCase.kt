package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllProductsUseCase(
    private val repository: ProductRepository
) {

    operator fun invoke(): Flow<List<Product>> {
        return repository.observeProducts()
    }
}