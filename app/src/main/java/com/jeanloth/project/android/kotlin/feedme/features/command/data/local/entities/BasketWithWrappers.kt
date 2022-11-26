package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket

class BasketWithWrappers(
    @Embedded val basketEntity: BasketEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "basketId",
        entity = ProductWrapperEntity::class
    )
    val wrappers: List<ProductWrapper>
)

fun BasketWithWrappers.asPojo() = Basket(
    basketId = basketEntity.id,
    label = basketEntity.label,
    price = basketEntity.price,
    wrappers = wrappers.map { it.asPojoWithCommand() }
)