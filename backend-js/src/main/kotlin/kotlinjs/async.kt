import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.await
import org.funktionale.Try
import kotlin.js.Promise

///**
// * Adapted from Raphael Stäbler's Pappel Node.js framework for Kotlin
// * https://github.com/blazer82/pappel-framework
// */
//
//package kotlinjs
//
//import org.funktionale.Disjunction
//import org.funktionale.Try
//import kotlin.coroutines.experimental.*
//import kotlin.js.Promise
//
//import kotlinx.coroutines.experimental.async
//
///**
// * Async functionality for async/kotlinjs.await ability.
// *
// * Starts an asynchronous execution [block] usually used in an async/kotlinjs.await construct.
// */
//fun async(block: suspend () -> Unit) {
//    block.startCoroutine(StandaloneCoroutine(EmptyCoroutineContext))
//}
//
///**
// * Await functionality for async/kotlinjs.await ability.
// *
// * Suspends current asychronous execution block and awaits the resolution of a Promise<[T]>.
// * Must be used within an [async] block.
// */
//suspend fun <T>await(block: () -> Promise<T>): T {
//    return suspendCoroutine { continuation ->
//        block().then {
//            value ->
//                continuation.resume(value)
//        }.catch {
//            error -> continuation.resumeWithException(error)
//        }
//    }
//}
//
suspend fun <T> tryAwait(block: () -> Promise<T>): Try<T> {
    return Try {
        async {
            block().await()
        }.await()
    }
}
//
///**
// * A dirty hack for the fact that
// */
//suspend fun <T> runBlocking(block: () -> Promise<T>): T {
//    val result =  await { block() }
//    //delay(10)
//}
//
//private class StandaloneCoroutine(override val context: CoroutineContext): Continuation<Unit> {
//    override fun resume(value: Unit) {}
//
//    override fun resumeWithException(error: Throwable) {}
//}
