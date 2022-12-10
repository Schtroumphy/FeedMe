package com.jeanloth.project.android.kotlin.feedme.core.extensions

import androidx.compose.ui.graphics.Color
import com.jeanloth.project.android.kotlin.feedme.core.theme.*

fun Int.toQuantityEditColor(quantityReference : Int) : Color = when {
    this == 0 -> Gray1
    this > quantityReference -> Violet2
    this == quantityReference  -> Vert0
    else -> Jaune1
}