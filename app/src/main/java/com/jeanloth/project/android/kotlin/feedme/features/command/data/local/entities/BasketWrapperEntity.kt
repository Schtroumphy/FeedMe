package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket_wrapper")
class BasketWrapperEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val basketId: Long = 0L,
    val commandId: Long = 0L,

    var quantity : Int,
    val status : String
)