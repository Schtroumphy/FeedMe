package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import androidx.compose.ui.graphics.Color
import com.jeanloth.project.android.kotlin.feedme.core.theme.*

enum class Status(val value : String, val color : Color) {
    TO_DO("A faire", Gray1),
    IN_PROGRESS("En cours", Orange1),
    DONE("Réalisée", Vert2),
    DELIVERED("Livrée", Violet1),
    CANCELED("Annulée", Red),
    PAYED("Payée", Purple80)
}