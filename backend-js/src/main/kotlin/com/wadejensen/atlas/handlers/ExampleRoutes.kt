package com.wadejensen.atlas.handlers

import com.wadejensen.atlas.model.Person
import com.wadejensen.example.SharedClass
import express.http.ExpressRequest
import express.http.ExpressResponse
import express.http.Method
import kotlinjs.http.Request
import kotlinjs.http.fetch
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.await
import org.w3c.fetch.Response
import kotlin.js.Promise

/**
 *  Express route handler listening on `https://hostname:port/async-get`.
 *  Makes a simple HTTP GET request asyncronously using the w3c window.fetch API.
 *  Uses a Kotlin coroutine wrapper around native JS `Promise`s, to mimic the ES7 async - await pattern.
 */
fun printPlatformSpecificPrimes(req: ExpressRequest, res: ExpressResponse, shared: SharedClass) {
    shared.platform = "Node.js"
    shared.printMe()
    println(shared.givePrimes(100))
}

fun asyncHttpGet(req: ExpressRequest, res: ExpressResponse) {
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
fun asyncHttpPost(req: ExpressRequest, res: ExpressResponse) {
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
            println("ExpressResponse object:")
            console.dir(data)
            res.send(JSON.stringify(data))
        }
    }
}

/** Express route handler listening on `https://hostname:port/parse-json`.
 *  Parses a JSON string into a Kotlin object (POJO) and then accesses fields in a type-safe way, sending the result
 *  in a text format back to the browser of the requester.
 */
fun parseJson(req: ExpressRequest, res: ExpressResponse) {
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
fun httpGetPromise(req: ExpressRequest, res: ExpressResponse) {
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
