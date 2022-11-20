package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.BasketWrapperDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductWrapperDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItem
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BaseRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketWrapperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class WrapperBasketRepositoryImpl @Inject constructor(
    private val dao : BasketWrapperDao,
) : BasketWrapperRepository {

    override fun observeBasketWrappers(): Flow<List<Wrapper<Basket>>> {
        // TODO
        return flowOf(emptyList())
    }

    override fun save(wrapper: Wrapper<Basket>) : Long {
        return dao.insert(BasketWrapperEntity(
            basketId = wrapper.item.id,
            commandId = wrapper.parentId,
            quantity = wrapper.quantity,
            status = wrapper.status.name
        ))
    }

    override fun save(wrappers: List<Wrapper<Basket>>) : Array<Long> {
        return dao.insertAll(wrappers.map { wrapper ->
            BasketWrapperEntity(
                basketId = wrapper.item.id,
                commandId = wrapper.parentId,
                quantity = wrapper.quantity,
                status = wrapper.status.name
            )
        })
    }

    override fun remove(wrapper: Wrapper<Basket>) {
        dao.delete(
            BasketWrapperEntity(
            id = wrapper.id,
            basketId = wrapper.item.id,
            quantity = wrapper.quantity,
            status = wrapper.status.name
            )
        )
    }
}
