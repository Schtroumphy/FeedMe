package com.jeanloth.project.android.kotlin.feedme.core.extensions

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItem
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

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

fun <T : WrapperItem> MutableList<Wrapper<T>>.updateWrapper(item: T, quantity : Int): MutableList<Wrapper<T>> {
    if(this.any { it.item == item }){
        if(quantity > 0) {
            this.firstOrNull { it.item == item}?.let {
                it.quantity = quantity
            }
        } else {
            this.removeIf { it.item == item }
        }

    } else {
        this.add(
            Wrapper(
                item = item,
                quantity = quantity
            )
        )
    }
    return this
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
