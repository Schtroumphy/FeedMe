package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import androidx.compose.ui.graphics.Color
import com.jeanloth.project.android.kotlin.feedme.core.theme.*

enum class CommandStatus(val label: String, val color: Color) {
    TO_DO("A faire", Gray1),
    LOADING("En cours", Orange1),
    FINISHED("Terminée", Vert2),
    DELIVERED("Livrée", Violet1),
    PAYED("Payée", Bleu1),
}