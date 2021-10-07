package com.amp.bemobile.data.features.transactions.repository

import com.amp.bemobile.data.features.transactions.service.TransactionService
import com.amp.bemobile.domain.core.functional.Either
import com.amp.bemobile.domain.core.functional.Failure
import com.amp.bemobile.domain.core.functional.map
import com.amp.bemobile.domain.features.transactions.TransactionRepository
import com.amp.bemobile.domain.features.transactions.models.RateView
import com.amp.bemobile.domain.features.transactions.models.TransactionView

class TransactionRepositoryImpl(private val service: TransactionService) : TransactionRepository {

    override suspend fun getRates(): Either<Failure, List<RateView>> =
        service.getRates().map { list -> list.map { it.toView() } }

    override suspend fun getTransactions(): Either<Failure, List<TransactionView>> =
        service.getTransactions().map { list -> list.map { it.toView() } }
}