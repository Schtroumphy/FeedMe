package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket

@Entity(tableName = "basket")
class BasketEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    var label : String = "",
    var price : Float = 0f,
)

fun BasketEntity.asPojo() : Basket {
    return Basket(
        basketId = id,
        label = label,
        price = price
    )
}
