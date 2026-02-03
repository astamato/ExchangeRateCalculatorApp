package com.astamato.exchangeratecalculatorapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astamato.exchangeratecalculatorapp.data.Ticker
import com.astamato.exchangeratecalculatorapp.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel
  @Inject
  constructor(
    private val repository: ExchangeRateRepository,
  ) : ViewModel() {
    private val _uiState = MutableStateFlow<ExchangeRateUiState>(ExchangeRateUiState.Loading)
    val uiState: StateFlow<ExchangeRateUiState> = _uiState

    init {
      fetchInitialData()
    }

    private fun fetchInitialData() {
      viewModelScope.launch {
        try {
          val currencies = repository.getAvailableCurrencies()
          val tickers = repository.getTickers(currencies.joinToString(","))
          val selectedCurrency: String = currencies.first()
          val initialTicker = tickers.find { it.book.endsWith(selectedCurrency.lowercase()) }
          val exchangeRate = initialTicker?.ask?.toBigDecimal() ?: BigDecimal.ONE
          val amount1 = "1"
          val amount2 = exchangeRate.toPlainString()

          _uiState.value =
            ExchangeRateUiState.Success(
              tickers = tickers,
              availableCurrencies = currencies,
              selectedCurrency = selectedCurrency,
              amount1 = amount1,
              amount2 = amount2,
            )
        } catch (e: Exception) {
          _uiState.value = ExchangeRateUiState.Error("Failed to fetch data")
        }
      }
    }

    fun onKeypadPress(key: String) {
      val currentState = _uiState.value
      if (currentState is ExchangeRateUiState.Success) {
        val currentAmount = if (currentState.activeField == 1) currentState.amount1 else currentState.amount2
        val newAmount =
          when (key) {
            "<" -> currentAmount.dropLast(1).ifEmpty { "0" }
            "." -> if (!currentAmount.contains(".")) "$currentAmount." else currentAmount
            else -> if (currentAmount == "0") key else currentAmount + key
          }
        if (currentState.activeField == 1) {
          onPrimaryAmountChange(newAmount)
        } else {
          onSecondaryAmountChange(newAmount)
        }
      }
    }

    private fun onPrimaryAmountChange(amount: String) {
      val currentState = _uiState.value
      if (currentState is ExchangeRateUiState.Success) {
        val newAmount = amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val selectedTicker = currentState.tickers.find { it.book.endsWith(currentState.selectedCurrency.lowercase()) }
        val exchangeRate = selectedTicker?.ask?.toBigDecimal() ?: BigDecimal.ONE

        val newAmount2 =
          if (currentState.isUsdcPrimary) {
            newAmount.multiply(exchangeRate)
          } else {
            if (exchangeRate != BigDecimal.ZERO) newAmount.divide(exchangeRate, 8, RoundingMode.HALF_UP) else BigDecimal.ZERO
          }

        _uiState.value =
          currentState.copy(
            amount1 = amount,
            amount2 = newAmount2.toPlainString(),
          )
      }
    }

    private fun onSecondaryAmountChange(amount: String) {
      val currentState = _uiState.value
      if (currentState is ExchangeRateUiState.Success) {
        val newAmount = amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val selectedTicker = currentState.tickers.find { it.book.endsWith(currentState.selectedCurrency.lowercase()) }
        val exchangeRate = selectedTicker?.ask?.toBigDecimal() ?: BigDecimal.ONE

        val newAmount1 =
          if (currentState.isUsdcPrimary) {
            if (exchangeRate != BigDecimal.ZERO) newAmount.divide(exchangeRate, 8, RoundingMode.HALF_UP) else BigDecimal.ZERO
          } else {
            newAmount.multiply(exchangeRate)
          }

        _uiState.value =
          currentState.copy(
            amount1 = newAmount1.toPlainString(),
            amount2 = amount,
          )
      }
    }

    fun onCurrencySelected(currency: String) {
      val currentState = _uiState.value
      if (currentState is ExchangeRateUiState.Success) {
        val newState = currentState.copy(selectedCurrency = currency)
        _uiState.value = newState
        recalculate(newState)
      }
    }

    fun onSwapCurrencies() {
      val currentState = _uiState.value
      if (currentState is ExchangeRateUiState.Success) {
        _uiState.value =
          currentState.copy(
            isUsdcPrimary = !currentState.isUsdcPrimary,
            amount1 = currentState.amount2,
            amount2 = currentState.amount1,
          )
      }
    }

    fun onActiveFieldChange(field: Int) {
      val currentState = _uiState.value
      if (currentState is ExchangeRateUiState.Success) {
        _uiState.value = currentState.copy(activeField = field)
      }
    }

    private fun recalculate(currentState: ExchangeRateUiState.Success) {
      if (currentState.activeField == 1) {
        onPrimaryAmountChange(currentState.amount1)
      } else {
        onSecondaryAmountChange(currentState.amount2)
      }
    }
  }

sealed class ExchangeRateUiState {
  object Loading : ExchangeRateUiState()

  data class Success(
    val tickers: List<Ticker>,
    val availableCurrencies: List<String>,
    val selectedCurrency: String,
    val amount1: String = "1",
    val amount2: String = "",
    val isUsdcPrimary: Boolean = true,
    val activeField: Int = 1,
  ) : ExchangeRateUiState()

  data class Error(
    val message: String,
  ) : ExchangeRateUiState()
}
