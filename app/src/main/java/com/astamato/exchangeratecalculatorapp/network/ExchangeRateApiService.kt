package com.astamato.exchangeratecalculatorapp.network

import com.astamato.exchangeratecalculatorapp.data.Ticker
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApiService {
    @GET("v1/tickers")
    suspend fun getTickers(
        @Query("currencies") currencies: String,
    ): List<Ticker>

    @GET("v1/tickers-currencies")
    suspend fun getAvailableCurrencies(): List<String>
}
