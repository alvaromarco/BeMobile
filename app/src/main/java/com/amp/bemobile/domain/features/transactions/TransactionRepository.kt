package com.amp.bemobile.domain.features.transactions

import com.amp.bemobile.domain.core.functional.Either
import com.amp.bemobile.domain.core.functional.Failure
import com.amp.bemobile.domain.features.transactions.models.RateView
import com.amp.bemobile.domain.features.transactions.models.TransactionView

interface TransactionRepository {

    suspend fun getRates() : Either<Failure, List<RateView>>

    suspend fun getTransactions() : Either<Failure, List<TransactionView>>
}