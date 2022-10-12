package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItemEntity

@Entity(tableName = "wrapper")
class WrapperEntity/*<out T: WrapperItemEntity>*/(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long = 0L,
    val basketId: Long = 0L,

    var quantity : Int,
    val status : String
)