package com.amp.bemobile.data.features.transactions.service.models

import com.amp.bemobile.domain.features.transactions.models.TransactionView

data class TransactionResponse(
    val sku: String = "",
    val amount: String = "",
    val currency: String = ""
) {
    fun toView(): TransactionView = TransactionView(
        sku = this.sku,
        amount = this.amount,
        currency = this.currency
    )
}