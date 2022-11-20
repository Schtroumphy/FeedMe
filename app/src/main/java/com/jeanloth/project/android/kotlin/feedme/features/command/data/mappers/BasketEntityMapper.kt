package com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers

import com.jeanloth.project.android.kotlin.feedme.core.interfaces.Mapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.ProductCategory.Companion.getProductCategoryFromCode
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

class BasketEntityMapper : Mapper<BasketEntity, Basket> {
    override fun from(entity: BasketEntity): Basket {
        return Basket(
            basketId = entity.id,
            label = entity.label,
            price = entity.price
        )
    }

    override fun to(pojo: Basket): BasketEntity {
        return BasketEntity(
            id = pojo.basketId,
            label = pojo.label,
            price = pojo.price
        )
    }
}