package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.CommandBasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket

class PopulatedCommandBasket(
    @Embedded val commandBasketEntity: CommandBasketEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "commandBasketId",
        entity = ProductWrapperEntity::class
    )
    val wrappers: List<ProductWrapper>
)

fun PopulatedCommandBasket.asPojo() = Basket(
    basketId = commandBasketEntity.id,
    label = commandBasketEntity.label,
    price = commandBasketEntity.price,
    wrappers = wrappers.map { it.asPojo() }
)

