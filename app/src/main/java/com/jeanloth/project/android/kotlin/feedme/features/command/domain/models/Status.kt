package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

enum class Status(val value : String) {
    TO_DO("A faire"),
    IN_PROGRESS("En cours"),
    DONE("Réalisée"),
    DELIVERED("Livrée"),
    CANCELED("Annulée"),
    PAYED("Payée")
}