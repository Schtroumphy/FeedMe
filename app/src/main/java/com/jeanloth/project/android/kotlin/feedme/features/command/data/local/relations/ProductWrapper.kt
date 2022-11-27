package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.ProductEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.asPojo
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

class ProductWrapper(
    @Embedded val wrapper: ProductWrapperEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id",
        entity = ProductEntity::class
    )
    val product: ProductEntity
)

fun ProductWrapper.asPojo() : Wrapper<Product> {
    return Wrapper(
        id = wrapper.id,
        parentId = wrapper.commandId,
        item = product.asPojo(),
        realQuantity = wrapper.realQuantity,
        quantity = wrapper.quantity,
        status = wrapper.status
    )
}