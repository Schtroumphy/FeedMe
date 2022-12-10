package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jeanloth.project.android.kotlin.feedme.core.database.CommandStatusConverter
import com.jeanloth.project.android.kotlin.feedme.core.database.DateTypeConverter
import com.jeanloth.project.android.kotlin.feedme.core.database.StatusConverter
import com.jeanloth.project.android.kotlin.feedme.core.database.WrapperTypeConverter
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperType

@Entity(tableName = "product_wrapper")
@TypeConverters(StatusConverter::class, WrapperTypeConverter::class)
class ProductWrapperEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long = 0L,

    // Parent id, wrapper associated to basket or command directly
    var basketId: Long = 0L,
    var commandBasketId: Long = 0L,
    val commandId: Long = 0L,

    // Wrapper type
    val wrapperType : WrapperType = WrapperType.NONE,

    var realQuantity : Int,
    var quantity : Int,
    val status : Status = Status.TO_DO
)