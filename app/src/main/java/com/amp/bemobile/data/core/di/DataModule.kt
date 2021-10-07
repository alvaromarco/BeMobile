package com.amp.bemobile.data.core.di

import com.amp.bemobile.data.core.utils.Constants
import com.amp.bemobile.data.features.transactions.repository.TransactionRepositoryImpl
import com.amp.bemobile.data.features.transactions.service.TransactionApi
import com.amp.bemobile.data.features.transactions.service.TransactionService
import com.amp.bemobile.domain.features.transactions.TransactionRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
    .addInterceptor(Interceptor { chain ->
        val original: Request = chain.request()

        val request: Request = original.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .method(original.method, original.body)
            .build()

        chain.proceed(request)
    })
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.ENDPOINT)
    .client(client)
    .addConverterFactory(
        MoshiConverterFactory.create(
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        )
    )
    .build()


val repositoryModule = module {
    single<TransactionRepository> { TransactionRepositoryImpl(service = get()) }
}

val serviceModule = module {
    single { TransactionService(api = get()) }
    single { retrofit.create(TransactionApi::class.java) }
}

