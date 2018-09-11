package com.wadejensen.atlas.flatmates.model

import kotlin.math.max
import kotlin.math.min

data class Search(
    val mode: String,
    val min_budget: Double,
    val max_bucket: Double,
    val top_left: String,
    val bottom_right: String)

data class MapMarkersRequestBody(val search: Search) {
    companion object {
        fun create(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double,
            requestType: ListingType,
            minPrice: Double,
            maxPrice: Double): MapMarkersRequestBody {

            val latMin = min(lat1, lat2)
            val lonMin = min(lon1, lon2)
            val latMax = max(lat1, lat2)
            val lonMax = max(lon1, lon2)

            val search = Search(
                mode = requestType.value,
                min_budget = minPrice,
                max_bucket = maxPrice,
                top_left = "${latMax}, ${lonMin}",
                bottom_right = "${latMin}, ${lonMax}")

            return MapMarkersRequestBody(search)
        }

    }
}
