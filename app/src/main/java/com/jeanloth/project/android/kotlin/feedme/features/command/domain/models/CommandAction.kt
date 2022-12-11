package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Euro
import androidx.compose.ui.graphics.vector.ImageVector
import com.jeanloth.project.android.kotlin.feedme.R


enum class CommandAction(val label: String, val iconButton: ImageVector, @StringRes val detailText : Int, ){
    DONE(
        label = "Terminer",
        iconButton = Icons.Rounded.Done,
        detailText = R.string.preparation_end_details_text
    ),
    DELIVER(
        label = "Livrer",
        iconButton = Icons.Default.Motorcycle,
        detailText = R.string.delivery_details_text
    ),
    PAY(
        label = "Payer",
        iconButton = Icons.Rounded.Euro,
        detailText = R.string.payment_details_text
    ),
    CANCEL(
        label = "Annuler",
        iconButton = Icons.Default.Cancel,
        detailText = R.string.cancel_details_text
    )
}