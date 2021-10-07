package com.amp.bemobile.domain.features.transactions.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionView(
    val sku: String = "",
    var amount: String = "",
    var currency: String = ""
) : Parcelable