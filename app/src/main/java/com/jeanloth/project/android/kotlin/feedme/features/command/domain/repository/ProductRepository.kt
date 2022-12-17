package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository


import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun save(client : Product)

    fun observeProducts() : Flow<List<Product>>

    fun remove(product : Product)

    fun syncProducts()
}