package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import java.time.LocalDate

data class Command(
    var id : Long = 0L,

    var status : Status = Status.TO_DO,
    val totalPrice : Int = 0,

    val productWrappers : List<Wrapper<Product>> = emptyList(),
    var basketWrappers : List<Wrapper<Basket>> = emptyList(),

    val clientId : Long = 0,
    val client : AppClient? = null,
    val deliveryDate : LocalDate = LocalDate.now(),
) {

    override fun toString(): String {
        return "Command : [Id : $id, status : $status, price : $totalPrice, product: $productWrappers, basketwrappers : $basketWrappers, " +
                "client : ${client.toNameString()}, deliverydate: $deliveryDate]"
    }

    override fun equals(other: Any?): Boolean {
        //return other is Command && other.productWrappers == productWrappers && other.basketWrappers == basketWrappers
        return false
    }

    companion object{

        fun Command?.toString2(): String {
            return "Command : [status : $this.status, product: $this.productWrappers, basketwrappers : $this.basketWrappers ]"
        }

        fun Command?.changeStatus(newStatus : Status) : Boolean {
            if(this == null) return false
            return when(newStatus) {
                Status.TO_DO -> this.status == Status.IN_PROGRESS
                Status.IN_PROGRESS -> {
                    // Check if there is realQuantity > 0
                    this.status == Status.TO_DO && (this.productWrappers.any { it.realQuantity > 0 } || this.basketWrappers.flatMap { it.item.wrappers }.any { it.realQuantity > 0 })
                }
                Status.DONE -> this.status == Status.IN_PROGRESS && this.productWrappers.all { it.realQuantity >= it.quantity } && this.basketWrappers.flatMap { it.item.wrappers }.all { it.realQuantity >= it.quantity }
                Status.DELIVERED -> this.status == Status.DONE
                Status.CANCELED -> this.status.order < Status.DELIVERED.order
                Status.PAYED -> this.status == Status.DELIVERED
            }
        }
    }
}
