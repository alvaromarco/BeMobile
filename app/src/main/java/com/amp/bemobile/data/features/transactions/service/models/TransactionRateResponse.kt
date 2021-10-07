package com.amp.bemobile.data.features.transactions.service.models

import com.amp.bemobile.domain.features.transactions.models.RateView

data class TransactionRateResponse(
    val from: String = "",
    val to: String = "",
    val rate: String = ""
) {
    fun toView(): RateView = RateView(
        from = this.from,
        to = this.to,
        rate = this.rate
    )
}