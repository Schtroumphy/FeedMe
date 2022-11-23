package com.jeanloth.project.android.kotlin.feedme.core.extensions

import java.time.*
import java.time.format.DateTimeFormatter

const val SLASH_DATE_FORMAT = "dd/MM/yyyy"

fun LocalDate?.formatToShortDate(): String = (this ?: LocalDate.now()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: ""