package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

interface GoogleMapRepository {

    suspend fun getPlacesByNominatim(input: String) : List<String>
}