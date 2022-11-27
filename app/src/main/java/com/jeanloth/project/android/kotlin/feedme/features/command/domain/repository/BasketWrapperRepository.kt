package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import kotlinx.coroutines.flow.Flow

interface BasketWrapperRepository {

    fun save(basketWrapper : Wrapper<Basket>) : Long

    fun save(basketWrappers : List<Wrapper<Basket>>) : Array<Long>

    fun update(basketWrappers : List<Wrapper<Basket>>)

    fun observeBasketWrappers() : Flow<List<Wrapper<Basket>>>

    fun observeBasketWrappersByCommandId(commandId: Long) : Flow<List<Wrapper<Basket>>>

    fun remove(basketWrapper : Wrapper<Basket>)

}