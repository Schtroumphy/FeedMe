package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services.GooglePlacesApi
import com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services.GooglePrediction
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.GoogleMapRepository
import javax.inject.Inject

class GoogleMapRepositoryImpl @Inject constructor(
    private val apiService : GooglePlacesApi
) : GoogleMapRepository {

    override suspend fun getPlaces(input: String): List<GooglePrediction> {
        return apiService.getPredictions(input = input).predictions
    }
}