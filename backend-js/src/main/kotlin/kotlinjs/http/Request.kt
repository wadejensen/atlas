package kotlinjs.http

import express.http.Method
import org.w3c.fetch.*
import kotlin.js.json

/**
 * A fake constructor for a dynamic JS RequestInit object.
 * Primarily used in calls to [[org.w3c.fetch]]
 */
data class Request(
    val method: Method = Method.GET,
    val headers: Map<String, String?>? = null,
    val body: Any? = null,
    val referrer: String? = null,
    val referrerPolicy: dynamic = null,
    val mode: RequestMode? = null,
    val credentials: RequestCredentials? = null,
    val cache: RequestCache? = null,
    val redirect: RequestRedirect? = null,
    val integrity: String? = null,
    val keepalive: Boolean? = null,
    val window: Any? = null)

fun Request.toRequestInit(): RequestInit {

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
