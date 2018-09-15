package com.wadejensen.atlas

import org.w3c.dom.events.Event
import kotlin.browser.document
import react.dom.render
import kotlin.browser.window
import kotlinx.html.*
import react.dom.*
import com.google.maps.GoogleMap
import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.js.json

//@JsModule("google.maps")
//external abstract class GoogleMaps {
//    fun
//}

class MapOptions {
    lateinit var center: LatitudeLongitude
    var zoom: Byte = 1
    fun center(init: LatitudeLongitude.() -> Unit) {
        center = LatitudeLongitude().apply(init)
    }
    fun toJson() = json("center" to center.toJson(), "zoom" to zoom)
}

class LatitudeLongitude() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    fun toJson() = json("lat" to latitude, "lng" to longitude)
}

class KotlinGoogleMap(element: Element?) : GoogleMap(element) {
    fun options(init: MapOptions.() -> Unit) {
        val options = MapOptions().apply(init)
        setOptions(options = options.toJson())
    }
}

fun kotlinGoogleMap(element: Element?, init: KotlinGoogleMap.() -> Unit) = KotlinGoogleMap(element).apply(init)

fun initMap(): KotlinGoogleMap {
    val div = document.getElementById("map")
    return kotlinGoogleMap(div) {
        options {
            zoom = 6
            center {
                latitude = 46.2050836
                longitude = 6.1090691
            }
        }
    }
}

/**
 * main function for JavaScript
 */
fun main(vararg args: String) {
    //nothing here, it's executed before DOM is ready


//    println("main() web called") //this is written to JavaScript browser's developer console
//    document.addEventListener("DOMContentLoaded", {
//        println("DOMContentLoaded")
//        document.getElementById("startButton")?.addEventListener("click", ::start)
//    })


    window.onload = {
        val root = document.getElementById("root") ?: throw IllegalStateException()

        render(root) {
            div {
                button(classes = "btn btn-primary") {
                    attrs.type = ButtonType.button
                }
            }

            game()

            googleMap()


            //buttons()

        }
        // We need to depend on the Google Maps API CDN script
        // Therefore we must do some eval hackery to get a Google Maps context

//        val googleMaps = js("google.maps")
//        console.dir(googleMaps)

        initMap()

        //val mapConstructor = googleMaps.Map

//        val map = googleMaps.Map(js("document.getElementById('map')"),
//            js("{center: {lat: -33.869, lng: 151.207}, zoom: 5 }"))

//        val map = js("""new google.maps.Map(
//                    document.getElementById('map'),
//                    {center: {lat: -33.869, lng: 151.207}, zoom: 15 })""")
//
//        console.dir(map)
    }
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
