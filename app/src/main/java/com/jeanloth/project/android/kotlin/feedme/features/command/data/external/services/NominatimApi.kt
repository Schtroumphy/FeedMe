package com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApi {

    @GET("search")
    suspend fun getPredictions(
        @Query("countrycodes") country: String = "fr",
        @Query("format") format: String = "jsonv2",
        @Query("addressdetails") addressdetails: String = "1",
        @Query("limit") limit: Int = 5,
        @Query("street") query: String
    ): Response<List<NominatimResponse>>
}