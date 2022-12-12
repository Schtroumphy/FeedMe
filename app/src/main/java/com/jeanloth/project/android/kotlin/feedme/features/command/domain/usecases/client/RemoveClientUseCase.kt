package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.client

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository

class RemoveClientUseCase(
    private val repository: AppClientRepository
) {

    operator fun invoke(client: AppClient) = repository.remove(client)
}