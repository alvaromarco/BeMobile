/**
 * Copyright (C) 2019 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amp.bemobile.domain.core.functional

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Error] or [Success].
 * FP Convention dictates that [Error] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Error
 * @see Success
 */
sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Error<out L>(val error: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Success<out R>(val entity: R) : Either<Nothing, R>()

    /**
     * Returns true if this is a Right, false otherwise.
     * @see Success
     */
    val isSuccess get() = this is Success<R>

    /**
     * Returns true if this is a Left, false otherwise.
     * @see Error
     */
    val isError get() = this is Error<L>

    /**
     * Creates a Left type.
     * @see Error
     */
    fun <L> error(a: L) = Error(a)


    /**
     * Creates a Left type.
     * @see Success
     */
    fun <R> success(b: R) = Success(b)

    /**
     * Applies fnL if this is a Left or fnR if this is a Right.
     * @see Error
     * @see Success
     */
/*    fun fold(onError: (L) -> Unit, onSuccess: (R) -> Unit): Any =
        when (this) {
            is Error -> onError(a)
            is Success -> onSuccess(b)
        }*/

    suspend fun fold(onError: suspend (L) -> Unit, onSuccess: suspend (R) -> Unit): Any =
        when (this) {
            is Error -> onError(error)
            is Success -> onSuccess(entity)
        }
}

/**
 * Composes 2 functions
 * See <a href="https://proandroiddev.com/kotlins-nothing-type-946de7d464fb">Credits to Alex Hart.</a>
 */
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

/**
 * Right-biased flatMap() FP convention which means that Right is assumed to be the default case
 * to operate on. If it is Left, operations like map, flatMap, ... return the Left value unchanged.
 */
fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Error -> Either.Error(error)
        is Either.Success -> fn(entity)
    }

suspend fun <T, L, R> Either<L, R>.flatMapSuspend(fn: suspend (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Error -> Either.Error(error)
        is Either.Success -> fn(entity)
    }

/**
 * Right-biased map() FP convention which means that Right is assumed to be the default case
 * to operate on. If it is Left, operations like map, flatMap, ... return the Left value unchanged.
 */
fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::success))

/** Returns the value from this `Right` or the given argument if this is a `Left`.
 *  Right(12).getOrElse(17) RETURNS 12 and Left(12).getOrElse(17) RETURNS 17
 */
fun <L, R> Either<L, R>.getOrElse(value: R): R =
    when (this) {
        is Either.Error -> value
        is Either.Success -> entity
    }


/**
 * Left-biased onFailure() FP convention dictates that when this class is Left, it'll perform
 * the onFailure functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
/*fun <L, R> Either<L, R>.onFailure(fn: (failure: L) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Error) fn(a) }*/

suspend fun <L, R> Either<L, R>.onFailure(fn: suspend (failure: L) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Error) fn(error) }

/**
 * Right-biased onSuccess() FP convention dictates that when this class is Right, it'll perform
 * the onSuccess functionality passed as a parameter, but, overall will still return an either
 * object so you chain calls.
 */
/*fun <L, R> Either<L, R>.onSuccess(fn: (success: R) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Success) fn(b) }*/

suspend fun <L, R> Either<L, R>.onSuccess(fn: suspend (success: R) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Success) fn(entity) }

