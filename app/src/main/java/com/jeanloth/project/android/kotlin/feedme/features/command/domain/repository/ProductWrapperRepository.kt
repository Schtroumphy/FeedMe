package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductWrapperRepository {

    fun save(product : Wrapper<Product>) : Long

    fun update(product : List<Wrapper<Product>>, isAssociatedToCommand: Boolean = true)

    fun saveAndGetIds(products : List<Wrapper<Product>>, isAssociatedToCommand: Boolean = false): Array<Long>

    fun save(products : List<Wrapper<Product>>, isAssociatedToCommand: Boolean = false)

    fun observeProducts(commandId: Long) : Flow<List<Wrapper<Product>>>

    fun remove(product : Wrapper<Product>)

}