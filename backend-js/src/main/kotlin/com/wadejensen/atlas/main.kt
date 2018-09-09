package com.wadejensen.atlas

import com.wadejensen.atlas.flatmates.FlatmatesClient
import com.wadejensen.atlas.flatmates.auth
import com.wadejensen.atlas.model.Person
import com.wadejensen.example.Console
import com.wadejensen.example.Math
import com.wadejensen.example.SharedClass
import org.w3c.fetch.Response
import express.Application
import kotlinjs.await
import kotlinjs.*
import express.http.Method
import kotlinjs.http.Request
import kotlinjs.http.fetch
import kotlin.js.Promise

import kotlinx.serialization.json.JSON


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
    val shared = SharedClass(Console(), Math())

    val app = Application()

    async {
        val flatmatesClient = FlatmatesClient("wade", "jensen").auth()
        println(flatmatesClient)
    }

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
            val resp: Response = await { fetch("https://jsonplaceholder.typicode.com/todos/1") }
            val data: Any? = await { resp.json() }

            data?.also {
                console.dir(data)
            }
            res.send(JSON.stringify(data as Any))
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
        val person: Person = JSON.parse<Person>(wade)
        async {
            val request = Request(
                method  = Method.POST,
                headers = mapOf("username" to "wjensen", "password" to "1234567"),
                body    = person)

            println("ExpressRequest object:")
            console.dir(request)

            val resp = await { fetch("https://jsonplaceholder.typicode.com/posts", request) }
            val data: Any? = await { resp.json() }
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

    app.startHttpServer(3000)
    val path = require("path")

    val staticWebContentPath = path.join(__dirname, "../../frontend-js/src/main/web") as String
    println("Serving content from: $staticWebContentPath")
    app.serveStaticContent(staticWebContentPath)

    println("Kotlin - Node.js webserver ready.")
}
