package com.jeanloth.project.android.kotlin.feedme.data.local.mappers

import com.jeanloth.project.android.kotlin.feedme.data.entities.AppClientEntity
import com.jeanloth.project.android.kotlin.feedme.domain.models.AppClient

class AppClientEntityMapper : Mapper<AppClientEntity, AppClient> {
    override fun from(entity: AppClientEntity): AppClient {
        return AppClient(
            idClient = entity.idClient,
            firstname = entity.firstname,
            lastname = entity.lastname,
            phoneNumber = entity.phoneNumber,
            isFavorite = entity.isFavorite
        )
    }

    override fun to(pojo: AppClient): AppClientEntity {
        return AppClientEntity(
            idClient = pojo.idClient,
            firstname = pojo.firstname,
            lastname = pojo.lastname,
            phoneNumber = pojo.phoneNumber,
            isFavorite = pojo.isFavorite
        )
    }
}