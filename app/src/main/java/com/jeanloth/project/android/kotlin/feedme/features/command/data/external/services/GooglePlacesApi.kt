package com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services

import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {
    @GET("maps/api/place/autocomplete/json")
    suspend fun getPredictions(
        @Query("key") key: String = "AIzaSyAJeIT2_n6WzNMOZzaKeAz3z5Lqtb1mQUY",
    @Query("types") types: String = "address",
    @Query("input") input: String
    ): GooglePredictionsResponse
}