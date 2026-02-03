package com.astamato.exchangeratecalculatorapp.network

import com.astamato.exchangeratecalculatorapp.network.converter.EmptyListOnErrorInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitInstance {
    private const val BASE_URL = "https://api.dolarapp.dev/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(EmptyListOnErrorInterceptor())
        .build()

    val api: ExchangeRateApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ExchangeRateApiService::class.java)
    }
}
