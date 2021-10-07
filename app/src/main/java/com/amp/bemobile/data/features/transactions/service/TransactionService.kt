package com.amp.bemobile.data.features.transactions.service

import com.amp.bemobile.data.core.base.BaseService
import com.amp.bemobile.data.features.transactions.service.models.TransactionRateResponse
import com.amp.bemobile.data.features.transactions.service.models.TransactionResponse
import com.amp.bemobile.domain.core.functional.Either
import com.amp.bemobile.domain.core.functional.Failure

class TransactionService(private val api: TransactionApi) : BaseService() {

    suspend fun getRates(): Either<Failure, List<TransactionRateResponse>> = safeCall(
        call = { api.getRates() },
        transform = { it }
    )

    suspend fun getTransactions(): Either<Failure, List<TransactionResponse>> = safeCall(
        call = { api.getTransactions() },
        transform = { it }
    )

}