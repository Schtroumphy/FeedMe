package com.jeanloth.project.android.kotlin.feedme.features.command.data.external.services

class Address (
    var house_number: String? = null,
    var road: String? = null,
    var suburb: String? = null,
    var city: String? = null,
    var municipality: String? = null,
    var county: String? = null,
    var iSO31662Lvl6: String? = null,
    var state: String? = null,
    var iSO31662Lvl4: String? = null,
    var region: String? = null,
    var postcode: String? = null,
    var country: String? = null,
    var countryCode: String? = null,
    var hamlet: String? = null,
    var town: String? = null,
    val additionalProperties: MutableMap<String?, Any?> = HashMap()) {

    fun setAdditionalProperty(name: String?, value: Any?) {
        additionalProperties.put(name, value)
    }
}

class NominatimResponse (
    var placeId: Int? = null,
    var licence: String? = null,
    var osmType: String? = null,
    var osmId: Int? = null,
    var boundingbox: List<String>? = null,
    var lat: String? = null,
    var lon: String? = null,
    var displayName: String? = null,
    var class_: String? = null,
    var type: String? = null,
    var importance: Double? = null,
    var address: Address? = null,
    val additionalProperties: MutableMap<String?, Any?> = HashMap()){

    fun setAdditionalProperty(name: String?, value: Any?) {
        additionalProperties.put(name, value)
    }
}