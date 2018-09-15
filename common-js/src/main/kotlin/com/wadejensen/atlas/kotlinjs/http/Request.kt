package com.wadejensen.atlas.kotlinjs.http

import org.w3c.fetch.*
import kotlin.js.json

/**
 * A fake constructor for a dynamic JS Request object.
 * Primarily used in calls to [[org.w3c.fetch]]
 */
fun Request(
    method: Method = Method.GET,
    headers: Map<String, String?>? = mapOf(),
    body: Any? = "",
    referrer: String? = "",
    referrerPolicy: dynamic = "no-referrer",
    mode: RequestMode? = RequestMode.NO_CORS,
    credentials: RequestCredentials? = RequestCredentials.OMIT,
    cache: RequestCache? = RequestCache.NO_STORE,
    redirect: RequestRedirect? = RequestRedirect.FOLLOW,
    integrity: String? = "",
    keepalive: Boolean? = null,
    window: Any? = null): RequestInit {

    val o = js("({})")

    o["method"] = method.value
    o["headers"] = headers
        ?.entries
        ?.map { it.toPair() }
        ?.toTypedArray()
        ?.let { json(*it) }
    o["body"] = JSON.stringify(body)
    o["referrer"] = referrer
    o["referrerPolicy"] = referrerPolicy
    o["mode"] = mode
    o["credentials"] = credentials
    o["cache"] = cache
    o["redirect"] = redirect
    o["integrity"] = integrity
    o["keepalive"] = keepalive
    o["window"] = window

    return o
}
