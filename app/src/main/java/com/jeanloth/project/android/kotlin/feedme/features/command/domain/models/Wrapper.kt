package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

data class Wrapper<T : WrapperItem>(
    var id: Long = 0L,
    var item : T,
    var parentId : Long = 0L,
    var realQuantity : Int = 0,
    var quantity : Int = 0,
    var wrapperType : WrapperType = WrapperType.NONE,
    val status : Status = Status.TO_DO
){
    val totalPrice = quantity * item.unitPrice

    val realQuantityMajored
        get() = if(realQuantity > quantity) quantity else realQuantity

    override fun equals(other: Any?): Boolean {
        return other is Wrapper<*> && this.item == other.item && this.realQuantity == other.realQuantity && this.quantity == other.quantity && this.status == other.status
    }

    override fun toString(): String {
        return "Wrapper : [id : $id, parentID: $parentId, Quantities : $realQuantity / $quantity, status: $status, item: $item]"
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + item.hashCode()
        result = 31 * result + parentId.hashCode()
        result = 31 * result + realQuantity
        result = 31 * result + quantity
        result = 31 * result + wrapperType.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + totalPrice.hashCode()
        return result
    }

    companion object{
        fun <T : WrapperItem> T.toWrapper() : Wrapper<T>{
            return Wrapper(item = this)
        }
    }
}

interface WrapperItem {
    val id: Long
    val unitPrice: Float
}

interface WrapperItemEntity
