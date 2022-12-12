package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

import com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services.GooglePrediction

interface GoogleMapRepository {

    suspend fun getPlaces(input: String) : List<GooglePrediction>
}