package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.BasketWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.CommandEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity

class CommandWithWrappers(
    @Embedded val commandEntity: CommandEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "commandId",
        entity = BasketWrapperEntity::class
    )
    val basketWrappers: List<PopulatedBasketWrapper>,

    @Relation(
        parentColumn = "id",
        entityColumn = "commandId",
        entity = ProductWrapperEntity::class
    )
    val productWrappers: List<ProductWrapperEntity>
)