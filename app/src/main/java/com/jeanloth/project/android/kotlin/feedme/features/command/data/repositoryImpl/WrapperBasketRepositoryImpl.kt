package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.BasketWrapperDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.asEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.asPojo
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketWrapperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WrapperBasketRepositoryImpl @Inject constructor(
    private val dao : BasketWrapperDao
) : BasketWrapperRepository {

    override fun observeBasketWrappers(): Flow<List<Wrapper<Basket>>> {
        // TODO
        return flowOf(emptyList())
    }

    override fun observeBasketWrappersByCommandId(commandId: Long): Flow<List<Wrapper<Basket>>> {

        // Get all basket With wrappers
        //val bw = basketDao.observeAll()

        // Get all basket wrappers associated to command id

        // Assign wrapper item
        return dao.observeByCommandId(commandId).map { it.map { it.asPojo() } }
    }

    override fun save(wrapper: Wrapper<Basket>) : Long {
        return dao.insert(wrapper.asEntity())
    }

    override fun save(wrappers: List<Wrapper<Basket>>) : Array<Long> {
        return dao.insertAll(wrappers.map { wrapper -> wrapper.asEntity() })
    }

    override fun update(basketWrappers: List<Wrapper<Basket>>) {
        dao.update(basketWrappers.map { wrapper -> wrapper.asEntity() })
    }

    override fun remove(wrapper: Wrapper<Basket>) {
        dao.delete( wrapper.asEntity())
    }
}
