package com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services

// External
data class GooglePredictionsResponse(
    val predictions: ArrayList<GooglePrediction>
)

// POJO
data class GooglePrediction(
    val description: String,
    val terms: List<GooglePredictionTerm>
)

data class GooglePredictionTerm(
    val offset: Int,
    val value: String
)