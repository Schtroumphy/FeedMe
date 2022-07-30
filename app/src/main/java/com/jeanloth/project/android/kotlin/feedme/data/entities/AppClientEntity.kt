package com.jeanloth.project.android.kotlin.feedme.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_client")
class AppClientEntity(
    @PrimaryKey
    var idClient: Long = 0,

    @ColumnInfo(name = "first_name")
    var firstname : String = "Adrien DELONNE",

    @ColumnInfo(name = "last_name")
    var lastname : String? = null,

    @ColumnInfo(name = "phone_number")
    var phoneNumber : Int? = null,

    @ColumnInfo(name = "is_favorite")
    var isFavorite : Boolean = false
)