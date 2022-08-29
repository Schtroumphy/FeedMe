package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket")
class BasketEntity (
    @PrimaryKey
    var basketId : Long = 0,

    var label : String = "",
    var price : Float = 0f,
    var isBasketType : Boolean = false, // Panier type ?

)