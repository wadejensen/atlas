package com.wadejensen.atlas

import com.wadejensen.atlas.flatmates.model.MapMarkersRequestBody
import com.wadejensen.atlas.flatmates.model.RoomType
import com.wadejensen.atlas.kotlinjs.http.Request
import com.wadejensen.atlas.kotlinjs.http.Method
import google.maps.*
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.await
import org.w3c.dom.events.Event
import kotlin.browser.document
import react.dom.render
import kotlin.browser.window
import kotlinx.html.*
import org.w3c.fetch.*
import react.dom.*
import kotlin.js.Promise

/**
 * main function for JavaScript
 */
fun main(vararg args: String) {
    //nothing here, it's executed before DOM is ready

    window.onload = {
        val root = document.getElementById("root") ?: throw IllegalStateException()

        render(root) {
            div {
                button(classes = "btn btn-primary") {
                    attrs.type = ButtonType.button
                }
            }

            game()
            googleMapComponent(divId = "map", widthPercent = 95, heightPixels = 500)

            //buttons()

        }
        // We need to depend on the Google Maps API CDN script
        // Therefore we must do some eval hackery to get a Google Maps context

        val map = google.maps.Map(document.getElementById("map"),
            MapOptions.create(
                lat = -33.869,
                lng = 151.207,
                zoom = 15))

        console.dir(map)

        val atlasMarkerPurple = atlasMarker(
            lat = -33.869,
            lon = 151.207,
            map = map,
            price = 400,
            fillRGB = RGB(100, 20, 200),
            outlineRGB = RGB(12, 20, 200))

        console.dir(atlasMarkerPurple)

        async {
            val req = Request(
                method = Method.POST,
                headers = mapOf("Content-Type" to "application/json;charset=UTF-8"),
                body = MapMarkersRequestBody.create(
                    lat1 = -33.878453691548835,
                    lon1 = 151.16001704415282,
                    lat2 = -33.90481527152859,
                    lon2 = 151.2626705475708,
                    roomType = RoomType.PRIVATE_ROOM,
                    minPrice = 100.0,
                    maxPrice = 2000.0))

            console.log("request")
            console.dir(req)

            val respPromise: Promise<Response> = window.fetch(
                //input = "${window.location.origin}/getMapMarkers",
                input = "http://localhost:4000/",
                init = req)

            val resp: Response = respPromise.await()
            console.log("mapMarkers0:")
            console.dir(resp)
            val payload0 = resp.text().await()
            console.dir(payload0)
        }

        // window.onload is rude and unnecessarily expects a return value of type "dynamic"
        undefined
    }
}

fun atlasMarker(lat: Double, lon: Double, map: google.maps.Map, price: Int, fillRGB: RGB, outlineRGB: RGB): Marker {
    return google.maps.Marker(
        MarkerOptions.create(
            lat = lat,
            lon = lon,
            map = map,
            icon = generatePriceMarkerIcon(
                price      = price,
                fillRGB    = fillRGB,
                outlineRGB = outlineRGB)))
}


/**
 * We start this function from button click
 */
fun start(ev: Event) {
    val shared = SharedClass(DOMConsole(), Math())
    shared.platform = "JavaScript Browser"
    shared.printMe()
    shared.printPrimes(100)
}
