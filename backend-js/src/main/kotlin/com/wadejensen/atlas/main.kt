package com.wadejensen.atlas

import com.wadejensen.atlas.flatmates.FlatmatesClient
import com.wadejensen.atlas.flatmates.model.ListingType
import com.wadejensen.atlas.model.Person
import com.wadejensen.example.Console
import com.wadejensen.example.Math
import com.wadejensen.example.SharedClass
import express.Application
import express.http.Method
import kotlinjs.http.Request
import kotlinjs.http.fetch
import kotlinjs.require
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.await
import org.funktionale.*
import org.w3c.fetch.Response
import kotlin.js.Promise
import kotlinx.serialization.json.JSON as Json

external val process: dynamic
external val __dirname: dynamic

/**
 * main function for JavaScript
 */
fun main(vararg args: String) {
    //nothing here
}

/**
 * We start this function from app.js"
 */
fun start() {

    val app = Application()
    val shared = SharedClass(Console(), Math())

    println(shared.givePrimes(2))

    val x = async {
        println("Start async")
        val resp: Response = fetch("https://jsonplaceholder.typicode.com/todos/1").await()
        val data: Any? = resp.json().await()
        console.dir(data!!)

        // Initial unsafe calls to authorise with provider APIs
        // Will throw exceptions and cause node process to exit in failure case
        val (flatmatesClient, nextClient) = initApiClients()

        console.dir(flatmatesClient)

        val listingsOrErr = flatmatesClient.getListings(
            lat1 = -33.878453691548835,
            lon1 = 151.16001704415282,
            lat2 = -33.90481527152859,
            lon2 = 151.2626705475708,
            listingType = ListingType.ROOMS,
            minPrice = 100.0,
            maxPrice = 2000.0)

        println("Num listings found")
        console.dir(listingsOrErr.success().size)
        println(listingsOrErr.success()[0].price)

        setupRoutes(app, shared, flatmatesClient)

        println(shared.givePrimes(4))

        println("All routes setup.")

        app.startHttpServer(3000)
        println("HTTP server started.")

        val path = require("path")
        val staticWebContentPath = path.join(__dirname, "../../frontend-js/src/main/web") as String
        println("Serving content from: $staticWebContentPath")
        app.serveStaticContent(staticWebContentPath)

        println("Kotlin - Node.js webserver ready.")
    }
}

suspend fun initApiClients(): Pair<FlatmatesClient, String> {
    println("Initialising API clients")

    println("Initialising flatmates.com.au.")
    val flatmatesClientOrErr: Try<FlatmatesClient> = FlatmatesClient.create()
    if (flatmatesClientOrErr.isFailure()) {
        println("""| Failed to create flatmates.com.au API client for reason:
                   | ${flatmatesClientOrErr.failure().message}
                   | cause:
                   | ${flatmatesClientOrErr.failure().cause}"""
            .trimMargin())
    }

    println("Created flatmates.com.au client")
    println(flatmatesClientOrErr)

    if (flatmatesClientOrErr.isSuccess()) {
        val flatmatesClient = flatmatesClientOrErr.success()
        println(flatmatesClient)
        return Pair(flatmatesClient, "Other clientelle")
    }
    else {
        println("Could not contact flatmates.com.au API")
        println(flatmatesClientOrErr.failure())
        println(flatmatesClientOrErr.failure().cause)

        process.exit(1)

        // We will never get here
        throw Exception("Could not contact flatmates.com.au API", flatmatesClientOrErr.failure())
    }
}

fun setupRoutes(app: Application, shared: SharedClass, flatmatesClient: FlatmatesClient): Unit {

    app.get("/primes") { _, _ ->
        shared.platform = "Node.js"
        shared.printMe()
        println(shared.givePrimes(100))
    }

    /**
     *  Express route handler listening on `https://hostname:port/async-get`.
     *  Makes a simple HTTP GET request asyncronously using the w3c window.fetch API.
     *  Uses a Kotlin coroutine wrapper around native JS `Promise`s, to mimic the ES7 async - await pattern.
     */
    app.get("/async-get") { _, res ->
        println("async-get route pinged")

        async {
            val resp: Response = fetch("https://jsonplaceholder.typicode.com/todos/1").await()
            val data: Any? = resp.json().await()

            data?.also {
                console.dir(data)
                res.send(JSON.stringify(data))
            }
        }
    }

    /**
     *  Express route handler listening on `https://hostname:port/async-post`.
     *  Makes a HTTP POST request asyncronously using the w3c window.fetch API.
     *  The fetch API is called from a typed wrapper which accepts Kotlin data class objects
     *  as the message body, a Map<String, String> for the headers, and an enum for the request type.
     *  Uses a Kotlin coroutine wrapper around native JS `Promise`s, to mimic the ES7 async - await pattern.
     */
    app.get("/async-post") { _, res ->
        println("async-post route pinged")

        val wade = "{\"name\":\"Wade Jensen\", \"age\": 22, \"address\": {\"streetNum\": 123, \"streetName\": \"Fake street\", \"suburb\": \"Surry Hills\", \"postcode\": 2010}}"
        val person = JSON.parse<Person>(wade)
        async {
            val request = Request(
                method  = Method.POST,
                headers = mapOf("username" to "wjensen", "password" to "1234567"),
                body    = person)

            println("ExpressRequest object:")
            console.dir(request)

            val resp = fetch("https://jsonplaceholder.typicode.com/posts", request).await()
            val data: Any? = resp.json().await()
            data?.also {
                println("Response object:")
                console.dir(data)
                res.send(JSON.stringify(data))
            }
        }
    }

    /** Express route handler listening on `https://hostname:port/parse-json`.
     *  Parses a JSON string into a Kotlin object (POJO) and then accesses fields in a type-safe way, sending the result
     *  in a text format back to the browser of the requester.
     */
    app.get("/parse-json") { _, res ->
        println("parse-json route pinged")

        val data = "{\"name\":\"Wade Jensen\", \"age\": 22, \"address\": {\"streetNum\": 123, \"streetName\": \"Fake street\", \"suburb\": \"Surry Hills\", \"postcode\": 2010}}"
        println(data)
        val person: Person = JSON.parse<Person>(data)
        res.send("""
            name    = ${person.name},
            age     = ${person.age},

            address.streetNum  = ${person.address.streetNum},
            address.streetName = ${person.address.streetName},
            address.suburb     = ${person.address.suburb},
            address.postcode   = ${person.address.postcode}
        """.trimIndent())
    }

    /**
     * Express route handler listening on `https://hostname:port/promise-get`.
     * Makes a simple HTTP GET request asyncronously using the w3c window.fetch API.
     * Handle the result using native JS `Promise`s, then send the result as a webpage response.
     */
    app.get("/promise-get") { _, res ->
        println("promise-get route pinged!")

        val resp: Promise<Response> = fetch("https://jsonplaceholder.typicode.com/todos/1")
        resp
            .then { result: Response -> result.json() as Any }
            .then { json -> JSON.stringify(json) }
            .then { strResult ->
                println(strResult)
                res.send(strResult)
            }
    }
}
