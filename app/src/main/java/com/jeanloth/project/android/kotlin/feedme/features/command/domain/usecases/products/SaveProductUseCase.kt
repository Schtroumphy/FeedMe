package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository

class SaveProductUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(product: Product){
        repository.save(product)
    }
}