package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_client")
class AppClientEntity(
    @PrimaryKey(autoGenerate = true)
    var idClient: Long,

    @ColumnInfo(name = "first_name")
    var firstname : String = "",

    @ColumnInfo(name = "phone_number")
    var phoneNumber : Int? = null
)