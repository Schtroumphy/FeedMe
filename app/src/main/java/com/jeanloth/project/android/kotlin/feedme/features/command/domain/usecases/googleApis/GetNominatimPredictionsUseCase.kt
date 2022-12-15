package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.googleApis

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.CommandAddress
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.GoogleMapRepository

class GetNominatimPredictionsUseCase(
    private val repository: GoogleMapRepository
) {

    suspend operator fun invoke(input : String): List<CommandAddress> {
        return repository.getPlacesByNominatim(input)
    }
}