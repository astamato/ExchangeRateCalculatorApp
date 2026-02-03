package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme
import com.astamato.exchangeratecalculatorapp.ui.util.Currency
import com.astamato.exchangeratecalculatorapp.ui.util.CurrencyUtils
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CurrencyRow(
  currency: Currency,
  amount: String,
  onRowClick: () -> Unit,
  onCurrencyClick: () -> Unit,
  isCurrencySelectable: Boolean,
  isActive: Boolean,
  modifier: Modifier = Modifier,
) {
  val numberFormat =
    NumberFormat.getNumberInstance(Locale.US).apply {
      maximumFractionDigits = 2
    }
  val formattedAmount = amount.toBigDecimalOrNull()?.let { numberFormat.format(it) } ?: amount

  val backgroundColor = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent

  Row(
    modifier =
      modifier
        .fillMaxWidth()
        .background(backgroundColor)
        .clickable(onClick = onRowClick)
        .padding(vertical = 16.dp, horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      modifier = Modifier.clickable(enabled = isCurrencySelectable, onClick = onCurrencyClick),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        painter = painterResource(id = currency.flag),
        contentDescription = currency.name,
        modifier = Modifier.size(24.dp),
      )
      Spacer(modifier = Modifier.padding(start = 16.dp))
      Text(text = currency.code, fontWeight = FontWeight.Bold)
      if (isCurrencySelectable) {
        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select currency")
      }
    }
    Spacer(modifier = Modifier.weight(1f))
    Text(
      text = "$$formattedAmount",
      fontSize = 22.sp,
      fontWeight = if (isActive) FontWeight.Bold else FontWeight.Light,
      letterSpacing = 1.1.sp,
    )
  }
}

@Preview(showBackground = true)
@Composable
fun CurrencyRowPreview() {
  ExchangeRateCalculatorAppTheme {
    CurrencyRow(
      currency = CurrencyUtils.getCurrency("MXN")!!,
      amount = "184,065.59",
      onRowClick = {},
      onCurrencyClick = {},
      isCurrencySelectable = true,
      isActive = true,
    )
  }
}
