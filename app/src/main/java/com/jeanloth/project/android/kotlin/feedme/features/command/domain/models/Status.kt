package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import androidx.compose.ui.graphics.Color
import com.jeanloth.project.android.kotlin.feedme.core.theme.*

enum class Status(val value : String, val color : Color, val order: Int) {
    TO_DO("A faire", Gray1, 1),
    IN_PROGRESS("En cours", Orange1, 2),
    DONE("Réalisée", Vert2, 3),
    DELIVERED("Livrée", Violet1, 4),
    CANCELED("Annulée", Red, 6),
    PAYED("Payée", Purple80, 5)
}