package com.amp.bemobile.data.features.transactions.service

import com.amp.bemobile.data.features.transactions.service.models.TransactionRateResponse
import com.amp.bemobile.data.features.transactions.service.models.TransactionResponse
import retrofit2.Response
import retrofit2.http.GET

interface TransactionApi {

    @GET("rates.json")
    suspend fun getRates(): Response<List<TransactionRateResponse>>

    @GET("transactions.json")
    suspend fun getTransactions(): Response<List<TransactionResponse>>

}