package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

class ProductWrapper(
    @Embedded val wrapper: WrapperEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id",
        entity = ProductEntity::class
    )
    val product: ProductEntity
)