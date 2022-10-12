package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket")
class BasketEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    var label : String = "",
    var price : Float = 0f,
)

// TODO Add wrapper entity ? or table for relation one-to-many with wrapperEntity