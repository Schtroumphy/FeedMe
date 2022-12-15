package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

data class CommandAddress(
    val description: String,
    val coordinates: Coordinates? = null
)
