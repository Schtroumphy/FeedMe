package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

data class AppClient(
    val idClient : Long = 0L,
    var firstname : String = "Adrien DELONNE",
    var phoneNumber : Int? = null,
) {
    override fun toString(): String {
        return this.toNameString()
    }
}

fun AppClient?.toNameString() : String {
    return "${this?.firstname}"
}