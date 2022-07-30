package com.jeanloth.project.android.kotlin.feedme.data.repository

import com.jeanloth.project.android.kotlin.feedme.data.local.dao.AppClientDao
import com.jeanloth.project.android.kotlin.feedme.data.local.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.feedme.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.domain.repository.AppClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppClientRepositoryImpl(
    private val dao : AppClientDao,
    private val mapper : AppClientEntityMapper,
) : AppClientRepository{
    override fun save(client: AppClient) {
        dao.insert(mapper.to(client))
    }

    override fun observeClients(): Flow<List<AppClient>> {
        return dao.observeAll().map { clients -> clients.map { mapper.from(it) } }
    }

}
