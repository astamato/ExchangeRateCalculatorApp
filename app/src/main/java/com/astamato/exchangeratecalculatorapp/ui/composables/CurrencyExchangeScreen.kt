package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ExchangeRateUiState
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.MainViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CurrencyExchangeScreen(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  StatelessCurrencyExchangeScreen(
    modifier = modifier,
    uiState = uiState,
    onCurrencySelected = viewModel::onCurrencySelected,
    onSwapCurrencies = viewModel::onSwapCurrencies,
    onActiveFieldChange = viewModel::onActiveFieldChange,
    onAmount1Change = viewModel::onPrimaryAmountChange,
    onAmount2Change = viewModel::onSecondaryAmountChange,
  )
}

@Composable
fun StatelessCurrencyExchangeScreen(
  uiState: ExchangeRateUiState,
  onCurrencySelected: (String) -> Unit,
  onSwapCurrencies: () -> Unit,
  onActiveFieldChange: (Int) -> Unit,
  onAmount1Change: (String) -> Unit,
  onAmount2Change: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    modifier = modifier,
    contentWindowInsets = WindowInsets.safeDrawing,
  ) { paddingValues ->
    Column(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(paddingValues),
    ) {
      Spacer(
        Modifier
          .height(30.dp)
      )
      when (uiState) {
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
            state = uiState,
            onCurrencySelected = onCurrencySelected,
            onSwapCurrencies = onSwapCurrencies,
            onActiveFieldChange = onActiveFieldChange,
            onAmount1Change = onAmount1Change,
            onAmount2Change = onAmount2Change,
          )
        }

        is ExchangeRateUiState.Error -> {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Text(text = uiState.message)
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
  onCurrencySelected: (String) -> Unit,
  onSwapCurrencies: () -> Unit,
  onActiveFieldChange: (Int) -> Unit,
  onAmount1Change: (String) -> Unit,
  onAmount2Change: (String) -> Unit,
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
    Spacer(
      Modifier
        .height(30.dp)
    )
    CurrencyInputSection(
      state = state,
      onSwapCurrencies = onSwapCurrencies,
      onActiveFieldChange = onActiveFieldChange,
      onCurrencyClick = { showBottomSheet = it },
      onAmount1Change = onAmount1Change,
      onAmount2Change = onAmount2Change,
    )

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

@Preview(showBackground = true, name = "Success State")
@Composable
fun StatelessCurrencyExchangeScreenSuccessPreview() {
  val tickers = emptyList<com.astamato.exchangeratecalculatorapp.data.Ticker>()
  val uiState =
    ExchangeRateUiState.Success(
      tickers = tickers,
      availableCurrencies = listOf("USDc", "MXN", "EURc", "COP"),
      selectedCurrency = "MXN",
      amount1 = "9999",
      amount2 = "184065.59",
    )
  ExchangeRateCalculatorAppTheme {
    StatelessCurrencyExchangeScreen(
      uiState = uiState,
      onCurrencySelected = {},
      onSwapCurrencies = {},
      onActiveFieldChange = {},
      onAmount1Change = {},
      onAmount2Change = {},
    )
  }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun StatelessCurrencyExchangeScreenLoadingPreview() {
  ExchangeRateCalculatorAppTheme {
    StatelessCurrencyExchangeScreen(
      uiState = ExchangeRateUiState.Loading,
      onCurrencySelected = {},
      onSwapCurrencies = {},
      onActiveFieldChange = {},
      onAmount1Change = {},
      onAmount2Change = {},
    )
  }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun StatelessCurrencyExchangeScreenErrorPreview() {
  ExchangeRateCalculatorAppTheme {
    StatelessCurrencyExchangeScreen(
      uiState = ExchangeRateUiState.Error("Failed to fetch data"),
      onCurrencySelected = {},
      onSwapCurrencies = {},
      onActiveFieldChange = {},
      onAmount1Change = {},
      onAmount2Change = {},
    )
  }
}
