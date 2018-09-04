package kotlinjs.http

import org.w3c.fetch.Response
import kotlin.js.Promise

/**
 *  A typed wrapper around the window.fetch browser API, ported to Node.js
 *  https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/fetch
 *  Used to make HTTP requests.
 *
 *  @property url The origin of the resource being fetched
 *  @return // TODO
 */
fun fetch(url: String): Promise<Response> {
    return Fetch.jsFetch(url) as Promise<Response>
}

/**
 *  A typed wrapper around the window.fetch browser API, ported to Node.js
 *  https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/fetch
 *  Used to make HTTP requests.
 *
 *  @property url The origin of the resource being fetched
 *  @property init The request
 *  @return // TODO
 */
fun fetch(url: String, request: Request): Promise<Response> {
    return Fetch.jsFetch(url, request.toRequestInit().asDynamic())
}

object Fetch {
    val jsFetch: dynamic = kotlinjs.require("node-fetch")
}
