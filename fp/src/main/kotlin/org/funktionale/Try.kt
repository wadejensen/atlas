//package com.wadejensen.fp
//
//sealed class Try<T> {
//    abstract fun isFailure(): Boolean
//
//    abstract fun isSuccess(): Boolean
//
//    abstract fun get(): T
//
//    operator fun invoke() = get()
//
//    fun getOrElse(f: () -> T): T = when (this) {
//        is Success -> get()
//        is Failure -> f()
//    }
//
//    fun orElse(f: () -> Try<T>): Try<T> = when (this) {
//        is Success -> this
//        is Failure -> f()
//    }
//
//    fun foreach(f: (T) -> Unit) {
//        if (isSuccess()) f(get())
//    }
//
//    fun onEach(f: (T) -> Unit): Try<T> = map {
//        f(it)
//        it
//    }
//
//    fun <X> flatMap(f: (T) -> Try<X>): Try<X> = when (this) {
//        is Success -> try {
//            f(get())
//        } catch (t: Throwable) {
//            Failure<X>(t)
//        }
//        is Failure -> Failure<X>(throwable)
//    }
//
//    fun <X> map(f: (T) -> X): Try<X> = flatMap { Success(f(it)) }
//
//    fun exists(predicate: Predicate<T>): Boolean = when (this) {
//        is Success -> predicate(get())
//        is Failure -> false
//    }
//
//    fun filter(predicate: Predicate<T>): Try<T> = when (this) {
//        is Success -> try {
//            val value = get()
//            if (predicate(value)) {
//                this
//            } else {
//                Failure<T>(NoSuchElementException("Predicate does not hold for $value"))
//            }
//        } catch (t: Throwable) {
//            Failure<T>(t)
//        }
//        is Failure -> this
//    }
//
//
//    fun rescue(f: (Throwable) -> Try<T>): Try<T> = when (this) {
//        is Success -> this
//        is Failure -> try {
//            f(throwable)
//        } catch (t: Throwable) {
//            Failure<T>(t)
//        }
//    }
//
//    fun handle(f: (Throwable) -> T): Try<T> = rescue { Success(f(it)) }
//
//    fun onSuccess(body: (T) -> Unit): Try<T> {
//        foreach(body)
//        return this
//    }
//
//    fun onFailure(body: (Throwable) -> Unit): Try<T> = when (this) {
//        is Success -> this
//        is Failure -> {
//            body(throwable)
//            this
//        }
//    }
//
//    fun toOption(): Option<T> = when (this) {
//        is Success -> Some(get())
//        is Failure -> None
//    }
//
//    fun toEither(): Either<Throwable, T> = when (this) {
//        is Success -> Either.right(get())
//        is Failure -> Either.left(throwable)
//    }
//
//    fun toDisjunction(): Disjunction<Throwable, T> = toEither().toDisjunction()
//
//    fun failed(): Try<Throwable> = when (this) {
//        is Success -> Failure(UnsupportedOperationException("Success"))
//        is Failure -> Success(throwable)
//    }
//
//    fun <X> transform(s: (T) -> Try<X>, f: (Throwable) -> Try<X>): Try<X> = when (this) {
//        is Success -> flatMap(s)
//        is Failure -> try {
//            f(throwable)
//        } catch (t: Throwable) {
//            Failure<X>(t)
//        }
//    }
//
//    fun <X> fold(s: (T) -> X, f: (Throwable) -> X): X = when (this) {
//        is Success -> try {
//            s(get())
//        } catch (t: Throwable) {
//            f(t)
//        }
//        is Failure -> f(throwable)
//    }
//
//
//    class Success<T>(private val t: T) : Try<T>() {
//        override fun get(): T = t
//
//        override fun isFailure() = false
//
//        override fun isSuccess() = true
//
//        override fun toString(): String {
//            return "Success($t)"
//        }
//
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (other !is Success<*>) return false
//
//            if (t != other.t) return false
//
//            return true
//        }
//
//        override fun hashCode(): Int {
//            return t?.hashCode() ?: 0
//        }
//
//
//    }
//
//    class Failure<T>(val throwable: Throwable) : Try<T>() {
//        override fun get(): T {
//            throw throwable
//        }
//
//        override fun isFailure() = true
//
//        override fun isSuccess() = false
//
//        override fun toString(): String {
//            return "Failure($throwable)"
//        }
//    }
//}
//
//fun <T> Try(body: () -> T): Try<T> = try {
//    Try.Success(body())
//} catch (t: Throwable) {
//    Try.Failure(t)
//}
//
//fun <T> Try<Try<T>>.flatten(): Try<T> = when (this) {
//    is Try.Success -> get()
//    is Try.Failure -> Try.Failure<T>(throwable)
//}
