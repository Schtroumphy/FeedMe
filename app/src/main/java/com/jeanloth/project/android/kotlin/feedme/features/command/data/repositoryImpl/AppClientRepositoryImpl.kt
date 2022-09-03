package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.AppClientDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.AppClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppClientRepositoryImpl @Inject constructor(
    private val dao : AppClientDao,
    private val mapper : AppClientEntityMapper,
) : AppClientRepository {

    override fun save(client: AppClient) {
        dao.insert(mapper.to(client))
    }

    override fun observeClients(): Flow<List<AppClient>> {
        return dao.observeAll().map { clients -> clients.map { mapper.from(it) } }
    }

    override fun remove(client: AppClient) {
        return dao.delete(mapper.to(client))
    }
}
