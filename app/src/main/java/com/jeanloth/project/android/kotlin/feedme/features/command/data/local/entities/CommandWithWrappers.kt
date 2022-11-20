package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

class CommandWithWrappers(
    @Embedded val commandEntity: CommandEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "commandId",
        entity = BasketWrapperEntity::class
    )
    val basketWrappers: List<BasketWrapperEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "commandId",
        entity = ProductWrapperEntity::class
    )
    val productWrappers: List<ProductWrapperEntity>
)