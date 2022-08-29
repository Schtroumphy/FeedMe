package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import androidx.lifecycle.ViewModel
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.SaveClientUseCase

class ClientVM(
    val saveClientUseCase: SaveClientUseCase
) : ViewModel() {

}