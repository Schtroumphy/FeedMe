package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket

@Entity(tableName = "command_basket")
class CommandBasketEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    var label : String = "",
    var price : Float = 0f,
)

fun CommandBasketEntity.asPojo() : Basket {
    return Basket(
        basketId = id,
        label = label,
        price = price
    )
}
