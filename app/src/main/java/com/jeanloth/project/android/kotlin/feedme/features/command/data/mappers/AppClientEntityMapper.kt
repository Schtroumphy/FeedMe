package com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers

import com.jeanloth.project.android.kotlin.feedme.core.interfaces.Mapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient

class AppClientEntityMapper : Mapper<AppClientEntity, AppClient> {
    override fun from(entity: AppClientEntity): AppClient {
        return AppClient(
            idClient = entity.idClient,
            firstname = entity.firstname,
            phoneNumber = entity.phoneNumber
        )
    }

    override fun to(pojo: AppClient): AppClientEntity {
        return AppClientEntity(
            idClient = pojo.idClient,
            firstname = pojo.firstname,
            phoneNumber = pojo.phoneNumber
        )
    }
}