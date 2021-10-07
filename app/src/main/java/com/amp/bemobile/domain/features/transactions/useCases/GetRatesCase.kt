package com.amp.bemobile.domain.features.transactions.useCases

import com.amp.bemobile.domain.core.functional.Either
import com.amp.bemobile.domain.core.functional.Failure
import com.amp.bemobile.domain.features.transactions.TransactionRepository
import com.amp.bemobile.domain.features.transactions.models.RateView

class GetRatesCase(private val repository: TransactionRepository) {

    suspend fun invoke(): Either<Failure, List<RateView>> = repository.getRates()
}