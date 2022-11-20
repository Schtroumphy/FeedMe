package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import java.time.LocalDate

data class Command(
    var id : Long = 0L,

    val status : Status = Status.TO_DO,
    val totalPrice : Int = 0,

    val productWrappers : List<Wrapper<Product>> = emptyList(),
    val basketWrappers : List<Wrapper<Basket>>,

    val clientId : Long,
    val deliveryDate : LocalDate = LocalDate.now(),
) {

    override fun toString(): String {
        return "Command : [Id : $id, status : $status, price : $totalPrice, product: $productWrappers, basketwrappers : $basketWrappers, " +
                "clientId : $clientId, deliverydate: $deliveryDate]"
    }
}
