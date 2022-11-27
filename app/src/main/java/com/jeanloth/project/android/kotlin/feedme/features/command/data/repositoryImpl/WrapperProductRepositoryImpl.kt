package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductWrapperDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.asPojo
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.asProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductWrapperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WrapperProductRepositoryImpl @Inject constructor(
    private val dao : ProductWrapperDao,
) : ProductWrapperRepository {

    override fun observeProducts(commandId: Long): Flow<List<Wrapper<Product>>> {
        return dao.observeProdctWrappers().map { it.map { it.asPojo() }.filter { it.parentId == commandId } }
    }

    override fun save(wrapper: Wrapper<Product>) : Long {
        return dao.insert(wrapper.asProductWrapperEntity(false))
    }

    override fun save(wrappers: List<Wrapper<Product>>, isAssociatedToCommand: Boolean) {
        return dao.inserts(wrappers.map { wrapper -> wrapper.asProductWrapperEntity(isAssociatedToCommand) })
    }

    override fun saveAndGetIds(wrappers: List<Wrapper<Product>>, isAssociatedToCommand: Boolean): Array<Long> {
        return dao.insertAll(wrappers.map { wrapper -> wrapper.asProductWrapperEntity(isAssociatedToCommand)})
    }

    /**
     * Update product wrapper - By editing command quantity products
     */
    override fun update(wrappers: List<Wrapper<Product>>, isAssociatedToCommand: Boolean) {
        dao.update(wrappers.map { wrapper -> wrapper.asProductWrapperEntity(isAssociatedToCommand) })
    }

    override fun remove(wrapper: Wrapper<Product>) {
        //dao.delete(wrapper.asProductWrapperEntity()) // Add isAssociatedToCommand ?
    }
}
