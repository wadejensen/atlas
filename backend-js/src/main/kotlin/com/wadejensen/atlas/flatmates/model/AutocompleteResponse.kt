package com.wadejensen.atlas.flatmates.model

/**
 * Auto-generated code for modelling the flatmates.com.au autocomplete API response
 */
data class AutocompleteResponse(
    val took: Int,
    val timed_out: Boolean,
    val _shards: Shards,
    val hits: Hits,
    val suggest: Suggest) {

    companion object {
        data class Shards(
            val total: Int,
            val successful: Int,
            val failed: Int)

        data class Hits(
            val total: Int,
            val max_score: Double,
            val hits: Array<Any>)

        data class Suggest(
            val location_suggest: Array<LocationSuggest>)

        data class LocationSuggest(
            val text: String,
            val offset: Int,
            val length: Int,
            val options: Array<Option>)

        data class Option(
            val text: String,
            val _index: String,
            val _type: String,
            val _id: String,
            val _score: Double,
            val _source: Source,
            val contexts: Contexts)

        data class Contexts(
            val location_type: Array<String>)

        data class Source(
            val id: Int,
            val state: String,
            val city: String,
            val suburb: String,
            val postcode: String,
            val country: String,
            val created_at: String,
            val updated_at: String,
            val latitude: Double,
            val longitude: Double,
            val polygon: Array<Any>,
            val location_type: String,
            val key: String,
            val average_rent: Int,
            val temp_latitude: Any,
            val temp_longitude: Any,
            val radius: Int,
            val name: Any,
            val short_name: Any,
            val synonyms: Array<Any>,
            val location: Array<Double>,
            val search_title: String,
            val short_title: String,
            val suggest: NestedSuggest)

        data class NestedSuggest(
            val input: Array<String>,
            val weight: Int,
            val contexts: Contexts)
    }
}
