package com.wadejensen.atlas.flatmates

import kotlinjs.await
import kotlinjs.http.fetch
import org.w3c.fetch.Response

//import arrow.*
//import arrow.core.*

//data class FlatmatesClient(val sessionId: String, val sessionToken: String)
//
//    init {
//        (this.sessionId, this.sessionToken) =  this.auth()
//    }
//}
//
//
//suspend fun auth(url: String = "https://flatmates.com.au"): Pair<String, String> {
//    val resp: Response = await { fetch(url) }
//
//    console.dir(resp.headers)
//
//    println("SESSION??:" + resp.headers.get("set-cookie"))
//    val cookie: String? = resp.headers.get("set-cookie")
//
//    return Pair(cookie, "str")
//}
