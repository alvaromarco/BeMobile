package com.amp.bemobile.data.core.base

import com.amp.bemobile.domain.core.functional.Either
import com.amp.bemobile.domain.core.functional.Failure
import com.amp.bemobile.view.core.utils.ConnectionDataManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException

abstract class BaseService : KoinComponent {

    private val connectionManager: ConnectionDataManager by inject()

    suspend fun <T, R> safeCall(
        call: suspend () -> Response<T>,
        transform: (T) -> R
    ): Either<Failure, R> = when (connectionManager.hasInternet()) {
        true -> safeApiResult(call, transform)
        false -> Either.Error(Failure.NetworkConnection)
    }

    private suspend fun <T, R> safeApiResult(
        call: suspend () -> Response<T>,
        transform: (T) -> R
    ): Either<Failure, R> = try {
        val response = call.invoke()

        if (response.isSuccessful) {
            response.body()?.run {
                Either.Success(transform(this))
            } ?: run { Either.Error(Failure.NetworkConnection) }
        } else {
            Timber.e("Error ${response.code()} - Response not successful")
            Either.Error(Failure.NetworkConnection)
        }

    } catch (socketException: SocketTimeoutException) {
        socketException.printStackTrace()
        Either.Error(Failure.NetworkConnection)
    } catch (exception: Throwable) {
        exception.printStackTrace()
        Either.Error(Failure.NetworkConnection)
    }
}