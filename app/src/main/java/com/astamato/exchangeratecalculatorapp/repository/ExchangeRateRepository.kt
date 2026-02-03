package com.astamato.exchangeratecalculatorapp.repository

import com.astamato.exchangeratecalculatorapp.data.Ticker

interface ExchangeRateRepository {
  suspend fun getTickers(currencies: String): List<Ticker>

  suspend fun getAvailableCurrencies(): List<String>
}
