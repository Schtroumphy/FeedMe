package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository

class SaveClientUseCase(
    private val repository: AppClientRepository
) {

    operator fun invoke(client: AppClient){
        repository.save(client)
    }
}