package com.wadejensen.atlas

import kotlinx.html.id
import kotlinx.html.style
import react.*
import react.dom.div

class GoogleMapComponent: RComponent<GoogleMapComponent.Props, RState>() {
    override fun RBuilder.render() {
        div(classes = "mx-auto my-4") {
            attrs.id = props.divId
            attrs.style = kotlinext.js.js {
                width = "${props.widthPercent}%"
                height = "${props.heightPixels}px"
            }
        }
    }

    interface Props: RProps {
        var divId: String
        var widthPercent: Int
        var heightPixels: Int
    }
}

/**
 * Creates a placeholder div for an embedded Google Map
 * @param divId The id of the html div to be created
 * @param widthPercent The percentage width of the browser to display the map
 * @param heightPixels The height in pixels of the map
 */
fun RBuilder.googleMapComponent(divId: String, widthPercent: Int, heightPixels: Int) = child(GoogleMapComponent::class) {
    attrs.divId = divId
    attrs.widthPercent = widthPercent
    attrs.heightPixels = heightPixels
}

