package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.AppClientDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.BasketDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.CommandDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.asPojo
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
    private val productMapper : ProductEntityMapper
) : CommandRepository {

    override fun save(command: Command) : Long {
        return dao.insert(commandEntityMapper.to(command))
    }

    override fun update(command: Command) {
        dao.update(commandEntityMapper.to(command))
    }

    override fun observeCommandById(id: Long): Flow<Command?> {
        val bw = basketDao.getBasketsWithWrappers()

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
                    id = bw.wrapper.id,
                    parentId = bw.wrapper.commandId,
                    item = bw.populatedBasket.asPojo(), // TODO Retrieve separately all product linked to this basketId to add to this item
                    realQuantity = bw.wrapper.realQuantity,
                    quantity = bw.wrapper.quantity,
                    status = bw.wrapper.status
                ) },
                deliveryDate = it.commandEntity.deliveryDate,
                client = clientMapper.from(clientDao.getById(it.commandEntity.clientId)),
                clientId = it.commandEntity.clientId
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
                        id = bw.wrapper.id,
                        parentId = bw.wrapper.commandId,
                        item = bw.populatedBasket.asPojo(), // TODO Retrieve separately all product linked to this basketId to add to this item
                        realQuantity = bw.wrapper.realQuantity,
                        quantity = bw.wrapper.quantity,
                        status = bw.wrapper.status
                    ) },
                    deliveryDate = it.commandEntity.deliveryDate,
                    client = clientMapper.from(clientDao.getById(it.commandEntity.clientId)),
                    clientId = it.commandEntity.clientId
                )
            }
        }
    }

    override fun remove(command: Command) {
        dao.delete(commandEntityMapper.to(command))
    }


}
