package com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers

import com.jeanloth.project.android.kotlin.feedme.core.interfaces.Mapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.CommandEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command

class CommandEntityMapper : Mapper<CommandEntity, Command> {
    override fun from(entity: CommandEntity): Command {
        return Command(
            id = entity.id,
            status = entity.status,
            totalPrice = entity.totalPrice,
            clientId = entity.clientId,
            deliveryDate = entity.deliveryDate
        )
    }

    override fun to(pojo: Command): CommandEntity {
        return CommandEntity(
            id = pojo.id,
            status = pojo.status,
            totalPrice = pojo.totalPrice,
            clientId = pojo.clientId,
            deliveryDate = pojo.deliveryDate
        )
    }
}