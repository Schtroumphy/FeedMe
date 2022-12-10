package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.core.extensions.asProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductWrapperDao
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductWrapperRepository
import javax.inject.Inject

class WrapperProductRepositoryImpl @Inject constructor(
    private val dao: ProductWrapperDao,
) : ProductWrapperRepository {

    override fun save(wrapper: Wrapper<Product>): Long = dao.insert(wrapper.asProductWrapperEntity())

    override fun save(wrappers: List<Wrapper<Product>>) = dao.inserts(
        wrappers.map { wrapper ->
            wrapper.asProductWrapperEntity()
        }
    )

    override fun saveAndGetIds(wrappers: List<Wrapper<Product>>): Array<Long> = dao.insertAll(
        wrappers.map { wrapper ->
            wrapper.asProductWrapperEntity()
        }
    )

    /**
     * Update product wrapper - By editing command quantity products
     */
    override fun update(wrappers: List<Wrapper<Product>>) = dao.update(
        wrappers.map { wrapper ->
            wrapper.asProductWrapperEntity()
        }
    )

    override fun remove(wrapper: Wrapper<Product>) = dao.delete(wrapper.asProductWrapperEntity())

}
