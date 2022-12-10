package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.CommandBasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper

class PopulatedCommandBasketWrapper(
    @Embedded val wrapper: BasketWrapperEntity,

    @Relation(
        parentColumn = "commandBasketId",
        entityColumn = "id",
        entity = CommandBasketEntity::class
    )
    val populatedBasket: PopulatedCommandBasket
)

fun PopulatedCommandBasketWrapper.asPojo() : Wrapper<Basket> {
    return Wrapper(
        id = wrapper.id,
        parentId = wrapper.commandId,
        item = populatedBasket.asPojo(),
        realQuantity = wrapper.realQuantity,
        quantity = wrapper.quantity,
        wrapperType = wrapper.wrapperType,
        status = wrapper.status
    )
}