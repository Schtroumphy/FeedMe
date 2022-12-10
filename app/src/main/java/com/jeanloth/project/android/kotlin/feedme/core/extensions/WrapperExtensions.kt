package com.jeanloth.project.android.kotlin.feedme.core.extensions

import android.util.Log
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapperEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItem
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperType
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

fun Wrapper<Product>.asProductWrapperEntity() : ProductWrapperEntity {

    var (commandId, basketId, commandBasketId) = listOf(0L, 0L, 0L)
    when(this.wrapperType){
        WrapperType.COMMAND_INDIVIDUAL_PRODUCT -> commandId = this.parentId
        WrapperType.COMMAND_BASKET_PRODUCT -> commandBasketId = this.parentId
        WrapperType.BASKET_PRODUCT -> basketId = this.parentId
        else -> { /* Do nothing for non product types */ }
    }

    return ProductWrapperEntity(
        id = this.id,
        productId = this.item.id,
        wrapperType = this.wrapperType,
        basketId = basketId,
        commandBasketId = commandBasketId,
        commandId = commandId,
        realQuantity = this.realQuantity,
        quantity = this.quantity,
        status = this.status
    )
}

fun MutableList<Wrapper<Product>>.updateProductWrapper(product: Product, quantity : Int): MutableList<Wrapper<Product>> {
    if(this.any { it.item == product }){
        if(quantity > 0) {
            this.firstOrNull { it.item == product}?.let {
                it.quantity = quantity
            }
        } else {
            this.removeIf { it.item == product }
        }
    } else {
        this.add(
            Wrapper(
                item = product,
                quantity = quantity
            )
        )
    }
    return this
}

fun <T : WrapperItem> MutableList<Wrapper<T>>.updateWrapper(item: T, quantity : Int, removeIfQuantityIsZero : Boolean = true): MutableList<Wrapper<T>> {
    val temp = this.toMutableList()
    if(this.any { it.item == item }){
        val index = temp.indexOfFirst { it.item == item }
        if(quantity > 0 || !removeIfQuantityIsZero) {
            temp[index] = temp[index]?.copy(quantity = quantity)
        } else {
            temp.removeIf { it.item == item }
        }

    } else {
        temp.add(
            Wrapper(
                item = item,
                quantity = quantity
            )
        )
    }
    return temp
}


fun <T : WrapperItem> List<Wrapper<T>>.progession() : Float {
    val quantityTotal = this.sumOf { it.quantity }
    val realQuantity = this.sumOf { it.realQuantityMajored }.toFloat()
    Log.i("PROGRESS", "${realQuantity / quantityTotal}")
    return realQuantity / quantityTotal
}


fun List<Wrapper<Product>>.toBasketDescription() : String {
    val sb = StringBuilder().apply {
        this@toBasketDescription.forEachIndexed { index, wrapper ->
            append("${wrapper.item.label} x${wrapper.quantity}")
            if(index != this@toBasketDescription.size - 1) append(", ")
        }
    }
    return sb.toString()
}
