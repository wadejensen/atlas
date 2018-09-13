package com.wadejensen.atlas.flatmates.model


/**
 * @param state
 * @param city
 * @param suburb
 * @param postcode
 * @param country
 * @param latitude
 * @param longitude
 * @param location_type Possible values: "suburb","city","university","tram_stop","train_station"
 * @param radius Approximate radius in km best suited to map view
 * @param search_title Formal name of the suggested location or point of interest (POI)
 * @param short_title  Abbreviated name of the suggested POI
 */
data class AutocompleteSuggestion(
    val state: String,
    val city: String,
    val suburb: String,
    val postcode: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val location_type: String,
    val radius: Int,
    val search_title: String,
    val short_title: String)
