package com.amp.bemobile.view.features.transactions.list

import androidx.lifecycle.viewModelScope
import com.amp.bemobile.domain.core.functional.getOrElse
import com.amp.bemobile.domain.features.transactions.models.RateView
import com.amp.bemobile.domain.features.transactions.models.TransactionView
import com.amp.bemobile.domain.features.transactions.useCases.GetRatesCase
import com.amp.bemobile.domain.features.transactions.useCases.GetTransactionsCase
import com.amp.bemobile.view.core.base.BaseViewModel
import com.amp.bemobile.view.core.utils.RateType
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

class TransactionListViewModel(
    private val getTransactionsCase: GetTransactionsCase,
    private val getRatesCase: GetRatesCase
) : BaseViewModel() {

    private val _transactionState: MutableSharedFlow<TransactionListState> =
        MutableSharedFlow(2, 3, BufferOverflow.DROP_OLDEST)
    val transactionState: SharedFlow<TransactionListState> = _transactionState

    private var rates: List<RateView> = mutableListOf()
    private var transactions: List<TransactionView> = mutableListOf()

    init {
        onTriggerEvent(TransactionListEvent.GetList)
    }

    fun onTriggerEvent(event: TransactionListEvent) {
        viewModelScope.launch {
            when (event) {
                TransactionListEvent.GetList -> {
                    getRates()
                    getTransactions()
                }
                is TransactionListEvent.ClickTransaction -> onClickTransaction(event.transaction)
            }
        }
    }

    private suspend fun getRates() {
        rates = getRatesCase.invoke().getOrElse(listOf())
    }

    private suspend fun getTransactions() {
        getTransactionsCase.invoke().fold(
            onError = {
                this.transactions = emptyList()
                _transactionState.emit(TransactionListState.ShowError)
            },
            onSuccess = { transactions ->
                this.transactions = transactions
                _transactionState.emit(TransactionListState.Load(this.transactions))
            }
        )
    }

    suspend fun onClickTransaction(transaction: TransactionView) {
        val filterTransaction = this.transactions
            .filter { it.sku == transaction.sku }
            .map { getConversion(it, RateType.EURO) }

        val decimalFormat = DecimalFormat("0.00")
        decimalFormat.roundingMode = RoundingMode.UP

        val total = decimalFormat.format(filterTransaction.sumOf { it.amount.toDouble() })

        _transactionState.emit(
            TransactionListState.ShowCurrentTransaction(filterTransaction, total)
        )
    }

    private fun getConversion(transaction: TransactionView, toRate: RateType): TransactionView =
        if (transaction.currency != toRate.value) {
            val filterRates = rates.filter { it.from == transaction.currency }

            // Find to euro
            filterRates.find { it.to == toRate.value }?.run {
                transaction.currency = toRate.value
                transaction.amount =
                    (transaction.amount.toDouble() * this.rate.toDouble()).toString()
                transaction
            } ?: run {
                transaction.currency = filterRates.first().to
                transaction.amount =
                    (transaction.amount.toDouble() * filterRates.first().rate.toDouble()).toString()

                // Try again with new value
                getConversion(transaction, toRate)
            }
        } else {
            transaction
        }
}