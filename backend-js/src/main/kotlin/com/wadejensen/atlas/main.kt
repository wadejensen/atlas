package com.wadejensen.atlas

import com.wadejensen.atlas.flatmates.FlatmatesClient
import com.wadejensen.atlas.flatmates.model.RoomType
import com.wadejensen.atlas.handlers.*
import express.Application
import express.http.ExpressRequest
import express.http.ExpressResponse
import com.wadejensen.atlas.kotlinjs.require
import kotlinx.coroutines.experimental.async
import org.funktionale.*
import kotlinx.serialization.json.JSON as Json

external val process: dynamic
external val __dirname: dynamic

const val staticResourcesPath = "../../frontend-js/src/main/web"
const val defaultPort = 3000
/**
 * main function for JavaScript
 */
fun main(vararg args: String) {
    //nothing here
}

/**
 * We start this function from atlas.js"
 */
fun start() {
    // Create Node.js Express app
    val app = Application()

    // Show off that I can share code between client and server
    val shared = SharedClass(Console(), Math())
    println(shared.givePrimes(2))

    async {
        val flatmatesClient = setupAtlas(app, shared)
        println("Kotlin - Node.js webserver ready.")

        // cutting edge
        val listingsOrErr = flatmatesClient.getListings(
            lat1 = -33.878453691548835,
            lon1 = 151.16001704415282,
            lat2 = -33.90481527152859,
            lon2 = 151.2626705475708,
            roomType = RoomType.PRIVATE_ROOM,
            minPrice = 100.0,
            maxPrice = 2000.0)

        println("Num listings found")
        console.dir(listingsOrErr.success().size)
        println(listingsOrErr.success()[0].price)
        console.dir(listingsOrErr.success()[0])

        val suggestions = flatmatesClient.autocomplete("redfern")
        console.dir(suggestions)
    }
}

suspend fun setupAtlas(app: Application, shared: SharedClass): FlatmatesClient {
    println("Initialising API clients...")
    val (flatmatesClient, nextClient) = initApiClients()
    println("API clients' initialisation successful.")

    println("Configuring express middleware router...")
    setupRoutes(app, shared, flatmatesClient)
    println("Express middleware configuration successful.")

    println("Starting HTTP server")
    startHttpServer(app)
    println("HTTP server launched")

    return flatmatesClient
}

fun setupRoutes(app: Application, shared: SharedClass, flatmatesClient: FlatmatesClient): Unit {
    app.get("/primes") { req, res -> printPlatformSpecificPrimes(req, res, shared) }
    app.get("/async-get") { req, res -> asyncHttpGet(req, res) }
    app.get("/async-post") { req: ExpressRequest, res: ExpressResponse -> asyncHttpPost(req, res) }
    app.get("/parse-json") { req: ExpressRequest, res: ExpressResponse -> parseJson(req, res) }
    app.get("/promise-get") { req, res -> httpGetPromise(req, res) }
}

fun startHttpServer(app: Application): Unit {
    // Attempt risky parsing of port environment variable
    val portOrErr = Try { process.env.PORT.toString().toInt() }

    val port = if (portOrErr.isSuccess()) {
        println("Using provided port: ${portOrErr.success()} from environment variable.")
        portOrErr.success()
    }
    else {
        defaultPort
    }

    app.startHttpServer(port)

    val path = require("path")
    val staticWebContentPath = path.join(__dirname, staticResourcesPath) as String
    println("Serving content from: $staticWebContentPath")
    app.serveStaticContent(staticWebContentPath)
}

suspend fun initApiClients(): Pair<FlatmatesClient, String> {
    println("Initialising flatmates.com.au.")
    val flatmatesClientOrErr: Try<FlatmatesClient> = FlatmatesClient.create()

    if (flatmatesClientOrErr.isFailure()) {
        throw RuntimeException("""| Failed to create flatmates.com.au API client for reason:
                   | ${flatmatesClientOrErr.failure().message}
                   | cause:
                   | ${flatmatesClientOrErr.failure().cause}""".trimMargin())
    }
    println("Created flatmates.com.au client")
    println(flatmatesClientOrErr)

    return Pair(flatmatesClientOrErr.success(), "Next client")
}
