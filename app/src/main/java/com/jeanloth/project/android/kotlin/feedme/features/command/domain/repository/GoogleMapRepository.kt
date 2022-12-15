package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.CommandAddress

interface GoogleMapRepository {

    suspend fun getPlacesByNominatim(input: String) : List<CommandAddress>
}