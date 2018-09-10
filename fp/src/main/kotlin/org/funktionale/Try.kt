package org.funktionale

typealias Try<T> = Disjunction<Throwable, T>

fun <T> Try<T>.isSuccess(): Boolean = this.isRight()
fun <T> Try<T>.isFailure(): Boolean = this.isLeft()

fun <T> Try<T>.success(): T         = this.component2()!!
fun <T> Try<T>.failure(): Throwable = this.component1()!!

inline fun <T> Try(body: () -> T): Try<T> = try {
    Disjunction.Right(body())
} catch (t: Throwable) {
    Disjunction.Left(t)
}
