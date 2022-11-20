package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

class BasketWrapper(
    @Embedded val wrapper: BasketWrapperEntity,

    @Relation(
        parentColumn = "basketId",
        entityColumn = "id",
        entity = BasketEntity::class
    )
    val basket: BasketEntity
)