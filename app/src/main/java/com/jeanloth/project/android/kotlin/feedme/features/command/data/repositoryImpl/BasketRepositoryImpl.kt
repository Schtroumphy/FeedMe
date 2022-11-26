package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import android.util.Log
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.BasketDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.ProductEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class BasketRepositoryImpl @Inject constructor(
    private val dao : BasketDao,
    //private val productEntityMapper: ProductEntityMapper,
) : BasketRepository {

    override fun save(basket: Basket) : Long {
        val entity = BasketEntity(
            label = basket.label,
            price = basket.price,
        )
        return dao.insert(entity)
    }

    override fun observeBaskets(commandId: Long): Flow<List<Basket>> {
        return observeBaskets().map { it.filter { it.wrappers.any { it.parentId == commandId } } }
    }

    override fun observeBaskets(): Flow<List<Basket>> {
        return dao.observeBasketsWithWrappers().map { baskets ->
            baskets.map {
                Basket(
                    basketId = it.basketEntity.id,
                    label = it.basketEntity.label,
                    price = it.basketEntity.price,
                    wrappers = it.wrappers.map {
                        Wrapper(
                            id = it.wrapper.id,
                            item = Product(
                                label = it.product.label,
                                unitPrice = it.product.unitPrice
                            ),
                            realQuantity = it.wrapper.realQuantity,
                            quantity = it.wrapper.quantity
                        )
                    }
                )
            }
        }
    }

    override fun remove(basket: Basket) {
        TODO("Not yet implemented")
    }


}
