package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jeanloth.project.android.kotlin.feedme.core.database.CommandStatusConverter
import com.jeanloth.project.android.kotlin.feedme.core.database.DateTypeConverter
import com.jeanloth.project.android.kotlin.feedme.core.database.StatusConverter
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status

@Entity(tableName = "product_wrapper")
@TypeConverters(StatusConverter::class)
class ProductWrapperEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long = 0L,
    val basketId: Long = 0L,
    val commandId: Long = 0L,

    var quantity : Int,
    val status : Status = Status.TO_DO
)