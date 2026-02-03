package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ExchangeRateUiState
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.MainViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CurrencyExchangeScreen(viewModel: MainViewModel = viewModel()) {
  val uiState by viewModel.uiState.collectAsState()

  Scaffold(
    contentWindowInsets = WindowInsets.safeDrawing,
  ) { paddingValues ->
    Column(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(paddingValues),
    ) {
      Text(
        text = "Exchange",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleLarge,
      )
      when (val state = uiState) {
        is ExchangeRateUiState.Loading -> {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            CircularProgressIndicator()
          }
        }

        is ExchangeRateUiState.Success -> {
          CurrencyExchangeContent(
            state = state,
            onKeypadPress = viewModel::onKeypadPress,
            onCurrencySelected = viewModel::onCurrencySelected,
            onSwapCurrencies = viewModel::onSwapCurrencies,
            onActiveFieldChange = viewModel::onActiveFieldChange,
          )
        }

        is ExchangeRateUiState.Error -> {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Text(text = state.message)
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyExchangeContent(
  state: ExchangeRateUiState.Success,
  onKeypadPress: (String) -> Unit,
  onCurrencySelected: (String) -> Unit,
  onSwapCurrencies: () -> Unit,
  onActiveFieldChange: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  var showBottomSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState()

  val selectedTicker = state.tickers.find { it.book.endsWith(state.selectedCurrency.lowercase()) }
  val exchangeRate = selectedTicker?.ask?.toBigDecimal() ?: BigDecimal.ONE
  val numberFormat =
    NumberFormat.getNumberInstance(Locale.US).apply {
      maximumFractionDigits = 2
    }
  val formattedExchangeRate = numberFormat.format(exchangeRate)

  Column(
    modifier = modifier.fillMaxSize(),
  ) {
    ExchangeCalculatorHeader(
      exchangeRate = formattedExchangeRate,
      selectedCurrency = state.selectedCurrency,
    )
    Spacer(modifier = Modifier.weight(0.5f))
    CurrencyInputSection(
      state = state,
      onSwapCurrencies = onSwapCurrencies,
      onActiveFieldChange = onActiveFieldChange,
      onCurrencyClick = { showBottomSheet = it },
    )
    Spacer(modifier = Modifier.weight(1f))

    NumericKeypad(onKeyPress = onKeypadPress)

    if (showBottomSheet) {
      ModalBottomSheet(
        onDismissRequest = { showBottomSheet = false },
        sheetState = sheetState,
      ) {
        ChooseCurrencySheet(
          availableCurrencies = state.availableCurrencies,
          selectedCurrency = state.selectedCurrency,
          onCurrencySelected = {
            onCurrencySelected(it)
            showBottomSheet = false
          },
          onClose = { showBottomSheet = false },
        )
      }
    }
  }
}
