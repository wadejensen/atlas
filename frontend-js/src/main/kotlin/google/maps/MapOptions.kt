package google.maps

import kotlin.js.Json

data class MapOptions(val center: Center, val zoom: Int) {
    companion object {
        data class Center(val lat: Double, val lng: Double)

        fun create(lat: Double, lng: Double, zoom: Int): Json {
            val options = MapOptions(
                center = Center(lat, lng),
                zoom = zoom)

            return JSON.parse(JSON.stringify(options))
        }
    }
}
