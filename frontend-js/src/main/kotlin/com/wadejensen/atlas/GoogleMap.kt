package com.wadejensen.atlas

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.await
import kotlinx.html.id
import kotlinx.html.style
import react.*
import react.dom.div
import kotlin.browser.document
import kotlin.js.Promise

class GoogleMap: RComponent<RProps, GoogleMap.State>() {
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

fun RBuilder.googleMap() = child(GoogleMap::class) {}
