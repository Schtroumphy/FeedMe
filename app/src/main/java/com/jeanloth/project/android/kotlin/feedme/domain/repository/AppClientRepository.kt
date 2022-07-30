package com.jeanloth.project.android.kotlin.feedme.domain.repository

import com.jeanloth.project.android.kotlin.feedme.domain.models.AppClient
import kotlinx.coroutines.flow.Flow

interface AppClientRepository {

    fun save(client : AppClient)

    fun observeClients() : Flow<List<AppClient>>
}