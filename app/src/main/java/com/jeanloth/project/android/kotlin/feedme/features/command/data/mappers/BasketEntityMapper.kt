package com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers

import com.jeanloth.project.android.kotlin.feedme.core.interfaces.Mapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket

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