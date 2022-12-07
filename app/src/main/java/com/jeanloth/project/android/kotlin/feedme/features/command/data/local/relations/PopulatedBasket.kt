package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.BasketEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket

class PopulatedBasket(
    @Embedded val basketEntity: BasketEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "basketId",
        entity = ProductWrapperEntity::class
    )
    val wrappers: List<ProductWrapper>
)

fun PopulatedBasket.asPojo() = Basket(
    basketId = basketEntity.id,
    label = basketEntity.label,
    price = basketEntity.price,
    wrappers = wrappers.map { it.asPojo(isAssociatedToCommand = false) }
)

