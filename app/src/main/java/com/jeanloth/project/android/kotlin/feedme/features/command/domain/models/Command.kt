package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import java.time.LocalDate

typealias Coordinates = Pair<String, String>

data class Command(
    var id : Long = 0L,

    var status : Status = Status.TO_DO,
    val totalPrice : Int = 0,

    val productWrappers : List<Wrapper<Product>> = emptyList(),
    var basketWrappers : List<Wrapper<Basket>> = emptyList(),

    val clientId : Long = 0,
    val client : AppClient? = null,
    val deliveryDate : LocalDate = LocalDate.now(),
    var deliveryAddress : String = "",
    var coordinates : Coordinates? = null
) {

    override fun toString(): String {
        return "Command : [Id : $id, status : $status, price : $totalPrice" +
                "client : ${client.toNameString()}, deliverydate: $deliveryDate, Address : $deliveryAddress, Coordinates $coordinates]"
    }

    override fun equals(other: Any?): Boolean {
        //return other is Command && other.productWrappers == productWrappers && other.basketWrappers == basketWrappers
        return false
        /*if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Command

        if (productWrappers != other.productWrappers || basketWrappers != other.basketWrappers) return false
        if (deliveryAddress != other.deliveryAddress ) return false // TODO Add fields to be checked if command can be editted

        return true */
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + totalPrice
        result = 31 * result + productWrappers.hashCode()
        result = 31 * result + basketWrappers.hashCode()
        result = 31 * result + clientId.hashCode()
        result = 31 * result + (client?.hashCode() ?: 0)
        result = 31 * result + deliveryDate.hashCode()
        result = 31 * result + deliveryAddress.hashCode()
        result = 31 * result + (coordinates?.hashCode() ?: 0)
        return result
    }

    companion object{

        fun Command?.toString2(): String {
            return "Command : [status : $this.status, product: $this.productWrappers, basketwrappers : $this.basketWrappers ]"
        }
    }
}
