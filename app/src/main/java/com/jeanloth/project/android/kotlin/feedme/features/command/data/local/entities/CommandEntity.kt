package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jeanloth.project.android.kotlin.feedme.core.database.CommandStatusConverter
import com.jeanloth.project.android.kotlin.feedme.core.database.DateTypeConverter
import com.jeanloth.project.android.kotlin.feedme.core.database.StatusConverter
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.CommandStatus
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import java.time.LocalDate

@Entity(tableName = "command")
@TypeConverters(DateTypeConverter::class, CommandStatusConverter::class)
class CommandEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    val status : CommandStatus = CommandStatus.TO_DO,
    val totalPrice : Int = 0,

    val clientId : Long,
    val deliveryDate : LocalDate = LocalDate.now().plusDays(1)
)

// TODO Add wrapper entity ? or table for relation one-to-many with wrapperEntity