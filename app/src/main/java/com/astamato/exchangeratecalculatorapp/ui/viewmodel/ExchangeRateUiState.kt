package com.astamato.exchangeratecalculatorapp.ui.viewmodel

import com.astamato.exchangeratecalculatorapp.data.Ticker

sealed class ExchangeRateUiState {
  object Loading : ExchangeRateUiState()

  data class Success(
    val tickers: List<Ticker>,
    val availableCurrencies: List<String>,
    val selectedCurrency: String,
    val amountPrimary: String = "1",
    val amountSecondary: String = "",
    val isUsdcPrimary: Boolean = true,
    val activeField: ActiveField = ActiveField.PRIMARY,
  ) : ExchangeRateUiState()

  data class Error(
    val message: String,
  ) : ExchangeRateUiState()
}
