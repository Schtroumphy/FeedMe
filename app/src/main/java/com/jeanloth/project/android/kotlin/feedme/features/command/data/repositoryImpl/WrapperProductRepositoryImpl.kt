package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductWrapperDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItem
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BaseRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductWrapperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class WrapperProductRepositoryImpl @Inject constructor(
    private val dao : ProductWrapperDao,
) : ProductWrapperRepository {

    override fun observeProducts(): Flow<List<Wrapper<Product>>> {
        // TODO
        return flowOf(emptyList())
    }

    override fun save(wrapper: Wrapper<Product>) : Long {
        return dao.insert(ProductWrapperEntity(
            productId = wrapper.item.id,
            basketId = wrapper.parentId,
            quantity = wrapper.quantity,
            status = wrapper.status
        ))
    }

    override fun save(wrappers: List<Wrapper<Product>>, isAssociatedToCommand: Boolean) : Array<Long> {
        return dao.insertAll(wrappers.map { wrapper ->
            ProductWrapperEntity(
                productId = wrapper.item.id,
                quantity = wrapper.quantity,
                status = wrapper.status,
                commandId = if(isAssociatedToCommand) wrapper.parentId else 0,
                basketId = if(isAssociatedToCommand) 0 else wrapper.parentId,
            )
        })
    }

    override fun remove(wrapper: Wrapper<Product>) {
        dao.delete(
            ProductWrapperEntity(
            id = wrapper.id,
                productId = wrapper.item.id,
            quantity = wrapper.quantity,
            status = wrapper.status
            )
        )
    }
}
