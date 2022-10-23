package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

class BasketWithWrappers(
    @Embedded val basketEntity: BasketEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "basketId",
        entity = WrapperEntity::class
    )
    val wrappers: List<ProductWrapper>
)