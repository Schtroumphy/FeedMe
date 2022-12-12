package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.googleApis

import com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services.GooglePrediction
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.GoogleMapRepository

class GetGooglePredictionsUseCase(
    private val repository: GoogleMapRepository
) {

    suspend operator fun invoke(input : String): List<GooglePrediction> {
        return repository.getPlaces(input)
    }
}