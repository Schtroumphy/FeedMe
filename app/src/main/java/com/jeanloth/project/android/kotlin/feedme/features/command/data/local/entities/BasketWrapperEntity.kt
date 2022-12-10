package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jeanloth.project.android.kotlin.feedme.core.database.StatusConverter
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperType

@Entity(tableName = "basket_wrapper")
@TypeConverters(StatusConverter::class)
class BasketWrapperEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val commandBasketId: Long = 0L,
    val commandId: Long = 0L,

    // Wrapper type
    val wrapperType : WrapperType = WrapperType.NONE,

    var realQuantity : Int,
    var quantity : Int,
    val status : Status = Status.TO_DO
)


fun Wrapper<Basket>.asEntity() : BasketWrapperEntity {
    return BasketWrapperEntity(
        id = this.id,
        commandBasketId = this.item.id,
        commandId = this.parentId,
        realQuantity = this.realQuantity,
        quantity = this.quantity,
        wrapperType = this.wrapperType,
        status = this.status
    )
}