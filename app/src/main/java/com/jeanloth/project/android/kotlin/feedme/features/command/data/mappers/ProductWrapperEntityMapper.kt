package com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers

import com.jeanloth.project.android.kotlin.feedme.core.interfaces.Mapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations.ProductWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity

class ProductWrapperEntityMapper : Mapper<ProductWrapperEntity, ProductWrapper> {
    override fun from(entity: ProductWrapperEntity): ProductWrapper {
        TODO()
    }

    override fun to(pojo: ProductWrapper): ProductWrapperEntity {
        TODO()
    }
}