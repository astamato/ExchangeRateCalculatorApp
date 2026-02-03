package com.astamato.exchangeratecalculatorapp.repository

import android.util.Log
import com.astamato.exchangeratecalculatorapp.data.Ticker
import com.astamato.exchangeratecalculatorapp.network.ExchangeRateApiService
import com.astamato.exchangeratecalculatorapp.schedulers.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepositoryImpl @Inject constructor(
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val api: ExchangeRateApiService,
) : ExchangeRateRepository {
    override suspend fun getTickers(currencies: String): List<Ticker> = withContext(dispatcherProvider.io) {
        try {
            api.getTickers(currencies)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAvailableCurrencies(): List<String> = withContext(dispatcherProvider.io) {
        try {
            val currencies = api.getAvailableCurrencies()
            currencies.ifEmpty {
                listOf("MXN", "ARS", "BRL", "COP")
            }
        } catch (e: Exception) {
            Log.e("ExchangeRateRepo", "getAvailableCurrencies() failed", e)
            listOf("MXN", "ARS", "BRL", "COP")
        }
    }
}
