package com.astamato.exchangeratecalculatorapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astamato.exchangeratecalculatorapp.repository.ExchangeRateRepository
import com.astamato.exchangeratecalculatorapp.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val repository: ExchangeRateRepository,
  private val logger: Logger,
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
        val amount2 = exchangeRate.setScale(2, RoundingMode.HALF_UP).toPlainString()

        _uiState.value =
          ExchangeRateUiState.Success(
            tickers = tickers,
            availableCurrencies = currencies,
            selectedCurrency = selectedCurrency,
            amountPrimary = amount1,
            amountSecondary = amount2,
          )
      } catch (e: Exception) {
        logger.e("MainViewModel", "Failed to fetch initial data", e)
        _uiState.value = ExchangeRateUiState.Error("Failed to fetch data")
      }
    }
  }

  fun onPrimaryAmountChange(amount: String) {
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
          amountPrimary = amount,
          amountSecondary = newAmount2.setScale(2, RoundingMode.HALF_UP).toPlainString(),
        )
    }
  }

  fun onSecondaryAmountChange(amount: String) {
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
          amountPrimary = newAmount1.setScale(2, RoundingMode.HALF_UP).toPlainString(),
          amountSecondary = amount,
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
          amountPrimary = currentState.amountSecondary,
          amountSecondary = currentState.amountPrimary,
        )
    }
  }

  fun onActiveFieldChange(field: ActiveField) {
    val currentState = _uiState.value
    if (currentState is ExchangeRateUiState.Success) {
      _uiState.value = currentState.copy(activeField = field)
    }
  }

  private fun recalculate(currentState: ExchangeRateUiState.Success) {
    if (currentState.activeField == ActiveField.PRIMARY) {
      onPrimaryAmountChange(currentState.amountPrimary)
    } else {
      onSecondaryAmountChange(currentState.amountSecondary)
    }
  }
}
