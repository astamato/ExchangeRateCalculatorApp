package com.astamato.exchangeratecalculatorapp.repository

import com.astamato.exchangeratecalculatorapp.data.Ticker
import com.astamato.exchangeratecalculatorapp.network.ExchangeRateApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApiService
) : ExchangeRateRepository {

    override suspend fun getTickers(currencies: String): List<Ticker> {
        return try {
            api.getTickers(currencies)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAvailableCurrencies(): List<String> {
        return try {
            val currencies = api.getAvailableCurrencies()
            currencies.ifEmpty {
                listOf("MXN", "ARS", "BRL", "COP")
            }
        } catch (e: Exception) {
            listOf("MXN", "ARS", "BRL", "COP")
        }
    }
}
