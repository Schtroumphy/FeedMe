package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.BasketDao
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CommandRepositoryImpl @Inject constructor(
    private val dao : BasketDao, // TODO Change to command
) : CommandRepository {

    override fun save(command: Command) : Long {
        // TODO
        return 0L
    }

    override fun observeCommands(): Flow<List<Command>> {
        return flowOf(emptyList())  // TODO
    }

    override fun remove(command: Command) {
        TODO("Not yet implemented")
    }


}
