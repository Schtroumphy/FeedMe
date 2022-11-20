package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductWrapperRepository {

    fun save(product : Wrapper<Product>) : Long

    fun save(products : List<Wrapper<Product>>) : Array<Long>

    fun observeProducts() : Flow<List<Wrapper<Product>>>

    fun remove(product : Wrapper<Product>)

}