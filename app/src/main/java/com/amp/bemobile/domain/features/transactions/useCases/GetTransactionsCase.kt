package com.amp.bemobile.domain.features.transactions.useCases

import com.amp.bemobile.domain.core.functional.Either
import com.amp.bemobile.domain.core.functional.Failure
import com.amp.bemobile.domain.features.transactions.TransactionRepository
import com.amp.bemobile.domain.features.transactions.models.TransactionView

class GetTransactionsCase(private val repository: TransactionRepository) {

    suspend fun invoke(): Either<Failure, List<TransactionView>> =
        repository.getTransactions()
}