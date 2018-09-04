/**
 * Adapted from Raphael Stäbler's Pappel Node.js framework for Kotlin
 * https://github.com/blazer82/pappel-framework
 */

package express

import express.http.ExpressRequest
import express.http.Response

external val process: dynamic
external val __dirname: dynamic

/**
 * Typed and slightly simplified wrapper around the popular Express middleware application used in node.js
 * https://expressjs.com/en/api.html#app
 *
 * Tweaked from the work of Raphael Stäbler
 * https://medium.com/@raphaelstbler/how-i-wrote-a-full-stack-webapp-for-node-js-and-react-with-kotlin-bd18c45ee517
 * See from https://github.com/blazer82/pappel-framework
 */
class Application {
    private val express: dynamic = kotlinjs.require("express")
    private val app: dynamic = express()
    private val router: ExpressRouter = express.Router()

    fun startHttpServer(port: Int): Unit {
        println("Starting server on port ${port}.")
        val bodyParser = kotlinjs.require("body-parser")
        app.use(bodyParser.raw())
        val http = kotlinjs.require("http")
        http.createServer(this.app)
        listen(port) // TODO accept callback lambda
        println("Server started successfully")
    }

    /**
     * Starts listening on [port].
     * @param port TCP port to listen on.
     * @return Promise<Unit>
     */
    fun listen(port: Int): dynamic = app.listen(port) // TODO: Add error handling

    /**
     * Handles all requests for [path].
     * @param path Path relative to the router's base path
     * @param callback Callback to handle requests
     */
    fun all(path: String, callback: (request: ExpressRequest, response: Response) -> Unit) {
        app.all(path) {
            req, res -> callback.invoke(express.Request(req), express.Response(res))
        }
    }

    /**
     * Handles DELETE requests for [path].
     * @param path Path relative to the router's base path
     * @param callback Callback to handle requests
     */
    fun delete(path: String, callback: (request: ExpressRequest, response: Response) -> Unit) {
        app.delete(path) {
            req, res -> callback.invoke(express.Request(req), express.Response(res))
        }
    }

    /**
     * Handles GET requests for [path].
     * @param path Path relative to the router's base path
     * @param callback Callback to handle requests
     */
    fun get(path: String, callback: (request: ExpressRequest, response: Response) -> Unit) {
        app.get(path) {
            req, res -> callback.invoke(express.Request(req), express.Response(res))
        }
    }

    /**
     * Registers a global request [callback].
     * @param callback Callback to handle requests
     */
    fun onRequest(callback: (request: ExpressRequest, response: Response, next: () -> Unit) -> Unit) {
        app.use {
            req, res, n -> callback.invoke(express.Request(req), express.Response(res), n as () -> Unit)
        }
    }

    /**
     * Handles POST requests for [path].
     * @param path Path relative to the router's base path
     * @param callback Callback to handle requests
     */
    fun post(path: String, callback: (request: ExpressRequest, response: Response) -> Unit) {
        app.post(path) {
            req, res -> callback.invoke(express.Request(req), express.Response(res))
        }
    }

    /**
     * Handles PUT requests for [path].
     * @param path Path relative to the router's base path
     * @param callback Callback to handle requests
     */
    fun put(path: String, callback: (request: ExpressRequest, response: Response) -> Unit) {
        app.put(path) {
            req, res -> callback.invoke(express.Request(req), express.Response(res))
        }
    }

    /**
     * Uses [router] for [path].
     * @param path Path relative to the router's base path
     * @param router Instance of another router to use for [path]
     */
    fun use(path: String, router: ExpressRouter) {
        app.use(path, router.expressRouter)
    }

    /**
     * Enables serving of static content beneath the specified filepath
     */
    fun serveStaticContent(path: String) = app.use(express.static(path))
}
