package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository

class SyncProductUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(){
        repository.syncProducts()
    }
}