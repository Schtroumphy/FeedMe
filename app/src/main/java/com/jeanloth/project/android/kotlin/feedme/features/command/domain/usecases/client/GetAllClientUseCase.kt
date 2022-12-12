package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.client

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository
import kotlinx.coroutines.flow.Flow

class GetAllClientUseCase(
    private val repository: AppClientRepository
) {

    operator fun invoke(): Flow<List<AppClient>> {
        return repository.observeClients()
    }
}