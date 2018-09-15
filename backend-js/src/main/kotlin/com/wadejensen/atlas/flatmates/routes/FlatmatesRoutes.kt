package com.wadejensen.atlas.flatmates.routes

import com.wadejensen.atlas.flatmates.FlatmatesClient
import express.http.ExpressRequest
import express.http.ExpressResponse
import kotlinx.coroutines.experimental.async

/**
 *  Express route handler listening on `https://hostname:port/async-post`.
 *  Makes a HTTP POST request asyncronously using the w3c window.fetch API.
 *  The fetch API is called from a typed wrapper which accepts Kotlin data class objects
 *  as the message body, a Map<String, String> for the headers, and an enum for the request type.
 *  Uses a Kotlin coroutine wrapper around native JS `Promise`s, to mimic the ES7 async - await pattern.
 */
fun mapMarkersHandler(flatmatesClient: FlatmatesClient, req: ExpressRequest, res: ExpressResponse) {
    println("mapMarkersHandler")

    async {

        //console.dir(req)

        println(req.method)
        println(req.parameters)
        println(req.body)

        //println(x)

        //flatmatesClient.getListings()

//        val request = Request(
//            method  = Method.POST,
//            headers = mapOf("username" to "wjensen", "password" to "1234567"),
//            body    = person)
//
//        println("ExpressRequest object:")
//        console.dir(request)
//
//        val resp = fetch("https://jsonplaceholder.typicode.com/posts", request).await()
//        val data: Any? = resp.json().await()
//        data?.also {
//            println("ExpressResponse object:")
//            console.dir(data)
//            res.send(JSON.stringify(data))
//        }

        res.send(JSON.stringify(req.body))
    }
}
