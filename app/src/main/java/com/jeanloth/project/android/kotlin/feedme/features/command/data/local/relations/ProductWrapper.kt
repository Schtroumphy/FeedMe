package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.ProductEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.asPojo
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperType
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
    val parentId = when(this.wrapper.wrapperType){
        WrapperType.COMMAND_INDIVIDUAL_PRODUCT -> this.wrapper.commandId
        WrapperType.COMMAND_BASKET_PRODUCT -> this.wrapper.commandBasketId
        WrapperType.BASKET_PRODUCT -> this.wrapper.basketId
        else -> 0L
    }
    return Wrapper(
        id = wrapper.id,
        parentId = parentId,
        item = product.asPojo(),
        realQuantity = wrapper.realQuantity,
        quantity = wrapper.quantity,
        wrapperType = this.wrapper.wrapperType,
        status = wrapper.status
    )
}
