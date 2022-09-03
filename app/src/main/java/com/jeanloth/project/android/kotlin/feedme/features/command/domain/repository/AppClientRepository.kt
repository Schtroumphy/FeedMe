package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import kotlinx.coroutines.flow.Flow

interface AppClientRepository {

    fun save(client : AppClient)

    fun observeClients() : Flow<List<AppClient>>

    fun remove(client : AppClient)
}