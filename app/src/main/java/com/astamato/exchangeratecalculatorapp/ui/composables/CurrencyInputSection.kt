package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.astamato.exchangeratecalculatorapp.R
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme
import com.astamato.exchangeratecalculatorapp.ui.util.CurrencyUtils
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ExchangeRateUiState

@Composable
fun CurrencyInputSection(
  state: ExchangeRateUiState.Success,
  onSwapCurrencies: () -> Unit,
  onActiveFieldChange: (Int) -> Unit,
  onCurrencyClick: (Boolean) -> Unit,
  onAmount1Change: (String) -> Unit,
  onAmount2Change: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val (primaryCurrency, secondaryCurrency) =
    if (state.isUsdcPrimary) {
      CurrencyUtils.getCurrency("USDc")!! to CurrencyUtils.getCurrency(state.selectedCurrency)!!
    } else {
      CurrencyUtils.getCurrency(state.selectedCurrency)!! to CurrencyUtils.getCurrency("USDc")!!
    }

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier.padding(horizontal = 16.dp)
  ) {
    Column {
      CurrencyRow(
        currency = primaryCurrency,
        amount = state.amount1,
        onAmountChange = onAmount1Change,
        onCurrencyClick = { onCurrencyClick(!state.isUsdcPrimary) },
        onFocusChanged = { if (it) onActiveFieldChange(1) },
        isCurrencySelectable = !state.isUsdcPrimary,
        isActive = state.activeField == 1,
      )
      Spacer(modifier = Modifier.padding(horizontal = 16.dp).height(16.dp))
      CurrencyRow(
        currency = secondaryCurrency,
        amount = state.amount2,
        onAmountChange = onAmount2Change,
        onCurrencyClick = { onCurrencyClick(state.isUsdcPrimary) },
        onFocusChanged = { if (it) onActiveFieldChange(2) },
        isCurrencySelectable = state.isUsdcPrimary,
        isActive = state.activeField == 2,
      )
    }
    Box(
      modifier = Modifier
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.background)
        .padding(4.dp)
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier =
          Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onSwapCurrencies),
      ) {
        Icon(
          painter = painterResource(id = R.drawable.swap_button),
          contentDescription = stringResource(id = R.string.swap_currencies_button),
          modifier = Modifier.size(16.dp),
          tint = MaterialTheme.colorScheme.onPrimary,
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun CurrencyInputSectionPreview() {
  val tickers = emptyList<com.astamato.exchangeratecalculatorapp.data.Ticker>()
  val state =
    com.astamato.exchangeratecalculatorapp.ui.viewmodel.ExchangeRateUiState.Success(
      tickers = tickers,
      availableCurrencies = CurrencyUtils.getAvailableCurrencyCodes(),
      selectedCurrency = "MXN",
      amount1 = "9999",
      amount2 = "184065.59",
    )
  ExchangeRateCalculatorAppTheme {
    CurrencyInputSection(
      state = state,
      onSwapCurrencies = {},
      onActiveFieldChange = {},
      onCurrencyClick = {},
      onAmount1Change = {},
      onAmount2Change = {},
    )
  }
}
