package com.wadejensen.atlas

import kotlinx.html.id
import kotlinx.html.style
import react.*
import react.dom.div

class GoogleMapDiv(id: String): RComponent<RProps, GoogleMapDiv.State>() {
    override fun RBuilder.render() {
        div(classes = "mx-auto my-4") {
            attrs.id = "map"
            attrs.style = kotlinext.js.js {
                width = "95%"
                height = "500px"
            }
        }
    }

    interface State: RState {
        var map: dynamic
    }
}

//fun RBuilder.googleMapDiv() = child(GoogleMapDiv::class) {}
