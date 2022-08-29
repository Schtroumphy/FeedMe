package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_wrapper")
class ProductWrapperEntity(
    @PrimaryKey
    var productWrapperId : Long = 0,

    var count : Int = 0,
    var attended : Int = 0
)