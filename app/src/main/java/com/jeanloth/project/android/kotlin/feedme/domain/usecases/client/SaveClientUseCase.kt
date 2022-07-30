package com.jeanloth.project.android.kotlin.feedme.domain.usecases.client

import com.jeanloth.project.android.kotlin.feedme.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.domain.repository.AppClientRepository

class SaveClientUseCase(
    private val repository: AppClientRepository
) {

    operator fun invoke(client: AppClient){
        repository.save(client)
    }
}