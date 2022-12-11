package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import androidx.compose.ui.graphics.Color
import com.jeanloth.project.android.kotlin.feedme.core.theme.*

enum class Status(val value : String, val primaryColor : Color, val secondaryColor : Color, val order: Int, val potentialAction : CommandAction? = null) {
    TO_DO(
        value = "A faire",
        primaryColor = Color.Black,
        secondaryColor = Gray1,
        order = 1,
        potentialAction = CommandAction.DONE
    ),
    IN_PROGRESS(
        value = "En cours",
        primaryColor = Orange1,
        secondaryColor = Orange2,
        order = 2,
        potentialAction = CommandAction.DONE
    ),
    DONE(
        value = "Réalisée",
        primaryColor = Vert2,
        secondaryColor = Vert3,
        order = 3,
        potentialAction = CommandAction.DELIVER
    ),
    DELIVERING(
        value = "En Livraison",
        primaryColor = Violet1,
        secondaryColor = Violet2,
        order = 4,
        potentialAction = CommandAction.PAY
    ),
    CANCELED(
        value = "Annulée",
        primaryColor = Red,
        secondaryColor = Rose1,
        order = 6
    ),
    PAYED(
        value = "Payée",
        primaryColor = BleuVert,
        secondaryColor = Bleu1,
        order = 5
    )
}
