package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient

sealed class ClientUiState {
    object EmptyList : ClientUiState()
    data class NoEmptyList(val clients : List<AppClient>): ClientUiState()
}
