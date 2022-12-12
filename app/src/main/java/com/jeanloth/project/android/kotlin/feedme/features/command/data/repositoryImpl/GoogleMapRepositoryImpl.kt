package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services.NominatimApi
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.GoogleMapRepository
import javax.inject.Inject

class GoogleMapRepositoryImpl @Inject constructor(
    private val nominatimService : NominatimApi
) : GoogleMapRepository {

    override suspend fun getPlacesByNominatim(input: String): List<String> {
        return nominatimService.getPredictions(query = input).body()?.filter {
            it.address?.road != null
        }?.map {
            // TODO Use string Builder instead ?
            "${it.address?.house_number ?: ""} ${it.address?.road ?: ""}, ${it.address?.postcode ?: ""} ${it.address?.city ?: it.address?.town ?: it.address?.country ?:""}"
        } ?: emptyList()
    }


}