package com.jeanloth.project.android.kotlin.feedme.domain.models

data class AppClient(
    val idClient : Long = 0L,
    var firstname : String = "Adrien DELONNE",
    var lastname : String? = null,
    var phoneNumber : Int? = null,
    var isFavorite : Boolean = false
) {
    override fun toString(): String {
        return this.toNameString()
    }
}

fun AppClient?.toNameString() : String {
    return "${this?.firstname} ${this?.lastname?.toUpperCase()}"
}