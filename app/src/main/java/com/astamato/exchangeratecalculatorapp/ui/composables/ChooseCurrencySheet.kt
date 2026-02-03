package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme
import com.astamato.exchangeratecalculatorapp.ui.util.CurrencyUtils

@Composable
fun ChooseCurrencySheet(
  availableCurrencies: List<String>,
  selectedCurrency: String,
  onCurrencySelected: (String) -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier.padding(16.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(text = "Choose currency", style = MaterialTheme.typography.titleLarge)
      IconButton(onClick = onClose) {
        Text(text = "X", fontSize = 20.sp)
      }
    }
    Spacer(modifier = Modifier.height(16.dp))
    availableCurrencies.forEach { currencyCode ->
      val currency = CurrencyUtils.getCurrency(currencyCode)
      if (currency != null) {
        Row(
          modifier =
            Modifier
              .fillMaxWidth()
              .clickable { onCurrencySelected(currencyCode) }
              .padding(vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Image(
            painter = painterResource(id = currency.flag),
            contentDescription = currency.name,
            modifier = Modifier.size(24.dp),
          )
          Spacer(modifier = Modifier.padding(start = 16.dp))
          Text(text = currency.name, modifier = Modifier.weight(1f))
          if (currencyCode == selectedCurrency) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Selected",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp),
            )
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ChooseCurrencySheetPreview() {
  ExchangeRateCalculatorAppTheme {
    ChooseCurrencySheet(
      availableCurrencies = listOf("USDc", "MXN", "EURc", "COP"),
      selectedCurrency = "MXN",
      onCurrencySelected = {},
      onClose = {},
    )
  }
}
