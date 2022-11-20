package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wrapper")
class WrapperEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val itemId: Long = 0L,
    val parentId: Long = 0L,

    var quantity : Int,
    val status : String
)