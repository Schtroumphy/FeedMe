package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.BasketDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.CommandBasketDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.CommandBasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.asPojo
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class BasketRepositoryImpl @Inject constructor(
    private val dao : BasketDao,
    private val commandBasketDao: CommandBasketDao,
) : BasketRepository {

    /** --------- Basket associated to command --------- **/
    override fun observeBaskets(commandId: Long): Flow<List<Basket>> {
        return commandBasketDao.observeCommandBasketsWithWrappers().map { baskets -> baskets.map { it.asPojo() } }.map { it.filter { it.wrappers.any { it.parentId == commandId } } }
    }

    override fun saveCommandBasket(basket: Basket): Long {
        val entity = CommandBasketEntity(
            label = basket.label,
            price = basket.price,
        )
        return commandBasketDao.insert(entity)
    }

    /** ---------  Basket not associated to command --------- **/
    override fun save(basket: Basket) : Long {
        val entity = BasketEntity(
            label = basket.label,
            price = basket.price,
        )
        return dao.insert(entity)
    }

    override fun observeBaskets(): Flow<List<Basket>> {
        return dao.observeBasketsWithWrappers().map { baskets ->
            baskets.map { it.asPojo() }
        }
    }

    override fun remove(basket: Basket) {
        TODO("Not yet implemented")
    }


}
