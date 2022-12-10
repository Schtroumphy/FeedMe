package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import androidx.compose.ui.graphics.Color
import com.jeanloth.project.android.kotlin.feedme.core.theme.*

enum class Status(val value : String, val primaryColor : Color, val secondaryColor : Color, val order: Int) {
    TO_DO("A faire", Color.Black, Gray1, 1),
    IN_PROGRESS("En cours", Orange1, Orange2,2),
    DONE("Réalisée", Vert2, Vert3,  3),
    DELIVERED("Livrée", Violet1, Violet2, 4),
    CANCELED("Annulée", Red, Rose1, 6),
    PAYED("Payée", BleuVert, BleuVert2,5)
}