package com.wadejensen.atlas.flatmates

import express.http.Method
import org.w3c.fetch.Response
//import kotlinjs.await
import kotlinjs.http.Request
import kotlinjs.http.fetch
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.await
import kotlinx.coroutines.experimental.delay
//import kotlinjs.tryAwait
import org.funktionale.Try
import kotlin.js.json
import kotlin.math.max
import kotlin.math.min

data class FlatmatesClient(val sessionId: String, val sessionToken: String) {
    suspend fun getListings(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        requestType: RequestType,
        minPrice: Double,
        maxPrice: Double): Try<List<Listing>> {


        // make API request

        // if results are saturated then break the request down

        // transform result into cannonical listing

        // ret result

        return TODO()
    }

    suspend fun mapMarkersApi(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        requestType: RequestType,
        minPrice: Double,
        maxPrice: Double): Try<List<FlatmatesListing>> {

        println("Creating request")

        // construct request body
        val request = Request(
            method = Method.POST,
            headers = mapOf(
                "Accept-Encoding" to "gzip, deflate, br",
                "Content-Type" to "application/json;charset=UTF-8",
                "Cookie" to this.sessionId,
                "X-CSRF-Token" to this.sessionToken
            ),
            body = GetMapMarkersRequestBody(lat1, lon1, lat2, lon2, requestType, minPrice, maxPrice)
        )

        // make request
        val resp= fetch("https://flatmates.com.au/map_markers", request).await()
        val data = resp.json().await()

//        data?.let {
//            console.dir(data)
//        }

        // parse data and handle errors
        return Try { listOf(Any()) }
    }

    companion object {

        /**
         * Asyncronous constructor of a [[FlatmatesClient]].
         * @param url The base url of Flatmates.com.au
         * @returns A FlatmatesClient if [[Success]]ful or a wrapped Throwable value if [[Failure]]
         */
        suspend fun create(url: String = "https://flatmates.com.au"): Try<FlatmatesClient> {
            return Try {
                async {
                    println("Before fetch create")
                    val response: Response = fetch(url).await()
                    //println("After fetch create")
                    //console.dir(response)
                    val sessionId = parseSessionId(response).get()
                    val sessionToken = parseSessionToken(response).get()

                    FlatmatesClient(sessionId, sessionToken)
                }.await()
            }
        }

        /**
         * Perform risky parsing of response header to determine session id for authentication
         * Sample cookie:
         * _session=InVBb0ZRQ05nZlBCNnI3Z1E0SkpncnNQQyI%3D--a29a6b2ea14a26a925da08acf912b82afe307681; path=/; expires=Sun, 09 Dec 2018 06:39:05 -0000; secure, _flatmates_session=8d5efaf0352d09453e11c6879c407774; domain=.flatmates.com.au; path=/; expires=Sun, 16 Sep 2018 06:39:05 -0000; secure; HttpOnly
         *
         * Desired result is this portion of the example above:
         * _flatmates_session=8d5efaf0352d09453e11c6879c407774
         *
         * @param resp HTTP response from Flatmates.com.au homepage
         * @returns The flatmates session id for authentication
         */
        private fun parseSessionId(resp: Response): Try<String> {
            return Try {
                val cookie: String? = resp.headers.get("set-cookie")

                println("the cookie=" + cookie)
                println("Match=" + cookie?.match("_flatmates_session=[a-zA-Z0-9]+"))

                // perform risky parsing of cookie in response header
                val sessionId = cookie
                    ?.match("_flatmates_session=[a-zA-Z0-9]+")
                    ?.get(0)

                if (sessionId == null) {
                    println("session id match failed")
                }
                else {
                    println("session id match succeeded")
                }

                println("DE WEEEEEEEE EVER GET HERE???")

                sessionId!!
            }
        }

        /**
         * Perform risky parsing of the Flatmates.com.au homepage for the csrf token used for authentication
         * Example of target div:
         * <meta name="csrf-token" content="ZquiBuMVNjCl+bGWeMO4GNI+CZMVGIZM0HgPe+3idZkJ315HrPNHQaM44j1mcYqriTS9dfL7+mKX41Y+81Sb5Q==" />
         */
        suspend fun parseSessionToken(resp: Response): Try<String> {
            return Try {
                val html = resp.text().await()
                //println(html)

                println()
                println("html match")
                println("")
                //console.dir(html.match(".*csrf-token.*")!!.count())

                val csrfTokenDiv = html
                    .match(".*csrf-token.*")
                    ?.get(0)

                if (csrfTokenDiv == null) {
                    println("csrfTokenDiv:" + csrfTokenDiv + "was null")
                }
                else {
                    println("token div was not null")
                }

                //println("Num matches=")
                //println(csrfTokenDiv?.match("\"[a-zA-Z0-9|=|+]+\"")?.count())

                val potentialMatch = csrfTokenDiv
                    ?.match("\"[a-zA-Z0-9|=|+|\\/]+\"")

                if (potentialMatch == null) {
                    println("potential matches empty")
                    println(csrfTokenDiv)
                    throw NoSuchElementException("Token regex pattern did not match div $csrfTokenDiv")
                }
                else {
                    println("potential matches exist")
                }

                //potentialMatch ?: throw NoSuchElementException("Token regex pattern did not match div $csrfTokenDiv")

                val token = csrfTokenDiv
                    ?.match("\"[a-zA-Z0-9|=|+|\\/]+\"")
                    ?.get(0)
                    ?.replace("\"", "")

                println("DIIIIIIIIIID WE EVER GET HERE???")

                token!!
            }
        }
    }
}

/**
 * Enum type for HTTP methods.
 */
enum class RequestType(val value: String) {
    ROOMS("rooms"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
}

typealias FlatmatesListing = Any
typealias Listing = Any

typealias GetMapMarkersRequestBody = Any

fun GetMapMarkersRequestBody(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double,
    requestType: RequestType,
    minPrice: Double,
    maxPrice: Double): dynamic {

    val latMin = min(lat1, lat2)
    val lonMin = min(lon1, lon2)
    val latMax = max(lat1, lat2)
    val lonMax = max(lon1, lon2)

    val body = js("({})")

    val search  = js("({})")
    search["mode"]       = requestType.value
    search["min_budget"] = minPrice
    search["max_budget"] = maxPrice
    // A seemingly Australia-centric API
    search["top_left"]     = "${latMax}, ${lonMin}"
    search["bottom_right"] = "${latMin}, ${lonMax}"

    body["search"] = search

    return body
}
