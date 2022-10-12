package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.WrapperDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.WrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.ProductEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WrapperProductRepositoryImpl @Inject constructor(
    private val dao : WrapperDao,
) : BaseRepository<Wrapper<Product>> {

    override fun observeProducts(): Flow<List<Wrapper<Product>>> {
        // TODO
        return flowOf(emptyList())
    }

    override fun save(wrapper: Wrapper<Product>) : Long {
        return dao.insert(WrapperEntity(
            productId = wrapper.item.id,
            basketId = wrapper.basketId,
            quantity = wrapper.quantity,
            status = wrapper.status.name
        ))
    }

    override fun save(wrappers: List<Wrapper<Product>>) : Array<Long> {
        return dao.insertAll(wrappers.map {wrapper ->
            WrapperEntity(
                productId = wrapper.item.id,
                basketId = wrapper.basketId,
                quantity = wrapper.quantity,
                status = wrapper.status.name
            )
        })
    }

    override fun remove(wrapper: Wrapper<Product>) {
        dao.delete(
            WrapperEntity(
            id = wrapper.id,
            productId = wrapper.item.id,
            quantity = wrapper.quantity,
            status = wrapper.status.name
            )
        )
    }
}
