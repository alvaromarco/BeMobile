package com.amp.bemobile.view.features.transactions.list

import com.amp.bemobile.domain.features.transactions.models.TransactionView

sealed class TransactionListEvent {
    object GetList : TransactionListEvent()
    data class ClickTransaction(val transaction : TransactionView): TransactionListEvent()
}

sealed class TransactionListState {
    object ShowLoading : TransactionListState()
    object ShowError : TransactionListState()
    data class Load(val transactions: List<TransactionView>) : TransactionListState()
    data class ShowCurrentTransaction(val transactions: List<TransactionView>, val total : String ): TransactionListState()
}

