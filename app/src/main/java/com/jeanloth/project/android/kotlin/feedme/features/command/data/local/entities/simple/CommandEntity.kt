package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jeanloth.project.android.kotlin.feedme.core.database.DateTypeConverter
import com.jeanloth.project.android.kotlin.feedme.core.database.StatusConverter
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.*
import java.time.LocalDate

@Entity(tableName = "command")
@TypeConverters(DateTypeConverter::class, StatusConverter::class)
class CommandEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    val status : Status = Status.TO_DO,
    val totalPrice : Int = 0,

    val clientId : Long,
    val deliveryDate : LocalDate = LocalDate.now().plusDays(1)
)

fun CommandEntity.toPojo(): Command{
    return Command(
        id = this.id,
        status = this.status,
        totalPrice = this.totalPrice,
        clientId = this.clientId,
        deliveryDate = this.deliveryDate
    )
}