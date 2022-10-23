package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

data class Basket(
    var id : Long = 0L,
    var label : String = "",
    var price : Float = 0f,
    var wrappers : List<Wrapper<Product>> = emptyList()
) : WrapperItem {
    override val unitPrice: Float
        get() = price

    override fun toString(): String {
        return "Basket : [Id : $id, Label : $label, price : $price, wrappers: $wrappers]"
    }
}
