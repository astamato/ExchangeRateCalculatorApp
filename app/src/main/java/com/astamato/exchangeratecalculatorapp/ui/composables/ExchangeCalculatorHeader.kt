package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ExchangeCalculatorHeader(
  exchangeRate: String,
  selectedCurrency: String,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier.padding(horizontal = 16.dp)) {
    Text(
      text = "Exchange calculator",
      style = MaterialTheme.typography.headlineLarge,
      fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = "1 USDc = $exchangeRate $selectedCurrency",
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.primary,
    )
  }
}

@PreviewLightDark
@Composable
fun ExchangeCalculatorHeaderPreview() {
  val numberFormat =
    NumberFormat.getNumberInstance(Locale.US).apply {
      maximumFractionDigits = 4
    }
  val formattedExchangeRate = numberFormat.format(18.409712234)

  ExchangeRateCalculatorAppTheme {
    ExchangeCalculatorHeader(
      exchangeRate = formattedExchangeRate,
      selectedCurrency = "MXN",
    )
  }
}
