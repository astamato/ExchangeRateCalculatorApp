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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    modifier = modifier.padding(horizontal = 16.dp),
  ) {
    Column {
      CurrencyRow(
        currency = primaryCurrency,
        amount = state.amount1,
        onRowClick = { onActiveFieldChange(1) },
        onCurrencyClick = { onCurrencyClick(!state.isUsdcPrimary) },
        isCurrencySelectable = !state.isUsdcPrimary,
        isActive = state.activeField == 1,
      )
      Spacer(modifier = Modifier.height(16.dp))
      CurrencyRow(
        currency = secondaryCurrency,
        amount = state.amount2,
        onRowClick = { onActiveFieldChange(2) },
        onCurrencyClick = { onCurrencyClick(state.isUsdcPrimary) },
        isCurrencySelectable = state.isUsdcPrimary,
        isActive = state.activeField == 2,
      )
    }
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
        contentDescription = "Swap currencies",
        modifier = Modifier.size(16.dp),
        tint = Color.White,
      )
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
      availableCurrencies = listOf("USDc", "MXN", "EURc", "COP"),
      selectedCurrency = "MXN",
      amount1 = "9,999",
      amount2 = "184,065.59",
    )
  ExchangeRateCalculatorAppTheme {
    CurrencyInputSection(
      state = state,
      onSwapCurrencies = {},
      onActiveFieldChange = {},
      onCurrencyClick = {},
    )
  }
}
