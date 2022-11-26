package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.AppClientDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.BasketDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.CommandDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.BasketEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.CommandEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.ProductEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CommandRepositoryImpl @Inject constructor(
    private val dao : CommandDao,
    private val clientDao: AppClientDao,
    private val productDao : ProductDao,
    private val basketDao : BasketDao,
    private val clientMapper: AppClientEntityMapper,
    private val commandEntityMapper : CommandEntityMapper,
    private val productMapper : ProductEntityMapper,
    private val basketMapper : BasketEntityMapper,
) : CommandRepository {

    override fun save(command: Command) : Long {
        return dao.insert(commandEntityMapper.to(command))
    }

    override fun update(command: Command) {
        dao.update(commandEntityMapper.to(command))
    }

    override fun getCommandById(id: Long): Command? {
        return null
    }

    override fun observeCommandById(id: Long): Flow<Command?> {
        val bw = basketDao.getBasketsWithWrappers()

        // Map with <BasketId, productAssociated>
        val basketProductMap : Map<Long, List<Wrapper<Product>>> = bw.groupBy { it.basketEntity.id }.mapValues { it.value.flatMap { it.wrappers }.map { pw ->
            Wrapper(
                item = productMapper.from(productDao.getById(pw.product.id)),
                realQuantity = pw.wrapper.realQuantity,
                quantity = pw.wrapper.quantity,
                status = pw.wrapper.status
            )
        }}

        return dao.observeCommandsWithWrappersById(id).filterNotNull().map {
            Command(
                id = it.commandEntity.id,
                status = it.commandEntity.status,
                totalPrice = it.commandEntity.totalPrice,
                productWrappers = it.productWrappers.map { pw -> Wrapper(
                    id = pw.id,
                    parentId = pw.commandId,
                    item = productMapper.from(productDao.getById(pw.productId)),
                    realQuantity = pw.realQuantity,
                    quantity = pw.quantity,
                    status = pw.status
                ) },
                basketWrappers = it.basketWrappers.map { bw -> Wrapper(
                    id = bw.id,
                    parentId = bw.commandId,
                    item = basketMapper.from(basketDao.getById(bw.basketId)).apply { this.wrappers = basketProductMap[this.basketId] ?: emptyList() }, // TODO Retrieve separately all product linked to this basketId to add to this item
                    realQuantity = bw.realQuantity,
                    quantity = bw.quantity,
                    status = bw.status
                ) },
                deliveryDate = it.commandEntity.deliveryDate,
                client = clientMapper.from(clientDao.getById(it.commandEntity.clientId))
            )
        }
    }

    override fun observeCommands(): Flow<List<Command>> {
        return dao.observeCommandsWithWrappers().map {
            it.map {
                Command(
                    id = it.commandEntity.id,
                    status = it.commandEntity.status,
                    totalPrice = it.commandEntity.totalPrice,
                    productWrappers = it.productWrappers.map { pw -> Wrapper(
                        item = productMapper.from(productDao.getById(pw.productId)),
                        realQuantity = pw.realQuantity,
                        quantity = pw.quantity,
                        status = pw.status
                    ) },
                    basketWrappers = it.basketWrappers.map { bw -> Wrapper(
                        item = basketMapper.from(basketDao.getById(bw.basketId)),
                        realQuantity = bw.realQuantity,
                        quantity = bw.quantity,
                        status = bw.status
                    ) },
                    deliveryDate = it.commandEntity.deliveryDate,
                    client = clientMapper.from(clientDao.getById(it.commandEntity.clientId))
                )
            }
        }
    }

    override fun remove(command: Command) {
        dao.delete(commandEntityMapper.to(command))
    }


}
