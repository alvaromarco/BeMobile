package com.amp.bemobile.view.core.di

import com.amp.bemobile.view.core.utils.ConnectionDataManager
import com.amp.bemobile.view.features.transactions.list.TransactionListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single { ConnectionDataManager(androidApplication()) }
}
val viewModelModule = module {
    viewModel { TransactionListViewModel(getTransactionsCase = get(), getRatesCase = get()) }
}