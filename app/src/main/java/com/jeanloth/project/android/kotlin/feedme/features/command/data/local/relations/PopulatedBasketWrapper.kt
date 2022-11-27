package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper

class PopulatedBasketWrapper(
    @Embedded val wrapper: BasketWrapperEntity,

    @Relation(
        parentColumn = "basketId",
        entityColumn = "id",
        entity = BasketEntity::class
    )
    val populatedBasket: PopulatedBasket
)

fun PopulatedBasketWrapper.asPojo() : Wrapper<Basket> {
    return Wrapper(
        id = wrapper.id,
        parentId = wrapper.commandId,
        item = populatedBasket.asPojo(),
        realQuantity = wrapper.realQuantity,
        quantity = wrapper.quantity,
        status = wrapper.status
    )
}