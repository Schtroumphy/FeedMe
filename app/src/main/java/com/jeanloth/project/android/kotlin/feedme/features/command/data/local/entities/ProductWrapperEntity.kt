package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_wrapper")
class ProductWrapperEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long = 0L,
    val basketId: Long = 0L,

    var quantity : Int,
    val status : String
)