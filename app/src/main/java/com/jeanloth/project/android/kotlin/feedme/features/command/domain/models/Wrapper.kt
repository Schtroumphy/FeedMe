package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

data class Wrapper<T : WrapperItem>(
    var id: Long = 0L,
    var item : T,
    var parentId : Long = 0L,
    var realQuantity : Int = 0,
    var quantity : Int = 0,
    val status : Status = Status.TO_DO
){
    val totalPrice = quantity * item.unitPrice

    override fun equals(other: Any?): Boolean {
        return other is Wrapper<*> && this.item == other.item && this.realQuantity == other.realQuantity && this.quantity == other.quantity && this.status == other.status
    }

    override fun toString(): String {
        return "Wrapper : [id : $id, parentID: $parentId, Quantities : $realQuantity / $quantity, status: $status, item: $item]"
    }

    companion object{
        fun <T : WrapperItem> T.toWrapper() : Wrapper<T>{
            return Wrapper(item = this)
        }
    }
}

fun Wrapper<Product>.asProductWrapperEntity(isAssociatedToCommand: Boolean) : ProductWrapperEntity {
    return ProductWrapperEntity(
        id = this.id,
        productId = this.item.id,
        basketId = if(isAssociatedToCommand) 0 else this.parentId,
        commandId = if(isAssociatedToCommand) this.parentId else 0,
        realQuantity = this.realQuantity,
        quantity = this.quantity,
        status = this.status
    )
}
interface WrapperItem {
    val id: Long
    val unitPrice: Float
}

interface WrapperItemEntity

enum class ProductWrapperStatus {
    TO_DO,
    IN_PROGRESS,
    DONE,
    CANCELED,
}