package com.amp.bemobile.domain.core.di

import com.amp.bemobile.domain.features.transactions.useCases.GetRatesCase
import com.amp.bemobile.domain.features.transactions.useCases.GetTransactionsCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetRatesCase(repository = get()) }
    factory { GetTransactionsCase(repository = get()) }
}