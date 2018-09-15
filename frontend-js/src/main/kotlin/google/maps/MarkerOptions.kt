package google.maps

import kotlin.js.Json
import kotlin.js.json

data class MarkerOptions(val position: Position, val map: google.maps.Map) {
    companion object {
        data class Position(val lat: Double, val lng: Double)

        fun create(lat: Double, lon: Double, map: google.maps.Map, icon: String? = null): Json {
            return json(
                "position" to Position(lat, lon),
                "map" to map,
                "icon" to icon)
        }
    }
}

