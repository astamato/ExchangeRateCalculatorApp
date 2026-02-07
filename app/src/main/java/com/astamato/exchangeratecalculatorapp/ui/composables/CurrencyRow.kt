package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astamato.exchangeratecalculatorapp.R
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

  Row(
    modifier = modifier
      .fillMaxWidth()
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
        modifier = Modifier.size(16.dp),
      )
      Spacer(modifier = Modifier.padding(start = 16.dp))
      Text(text = currency.code, fontWeight = FontWeight.Bold, fontSize = 16.sp)
      Spacer(modifier = Modifier.padding(start = 8.dp))
      if (isCurrencySelectable) {
        Icon(
          painter = painterResource(id = R.drawable.chevron_button),
          contentDescription = "Select currency",
        )
      }
    }
    Spacer(modifier = Modifier.weight(1f))
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = "$$formattedAmount",
        fontSize = 16.sp,
        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Light,
      )
      if (isActive) {
        BlinkingCursor()
      }
    }
  }
}

@Composable
private fun BlinkingCursor() {
  val infiniteTransition = rememberInfiniteTransition(label = "cursor")
  val alpha by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(500),
      repeatMode = RepeatMode.Reverse,
    ),
    label = "cursorAlpha",
  )

  Box(
    modifier = Modifier
      .padding(start = 2.dp)
      .width(2.dp)
      .height(24.dp)
      .alpha(alpha)
      .background(MaterialTheme.colorScheme.primary),
  )
}

@Preview(showBackground = true)
@Composable
fun CurrencyRowPreview() {
  ExchangeRateCalculatorAppTheme {
    CurrencyRow(
      currency = CurrencyUtils.getCurrency("MXN")!!,
      amount = "184065.59",
      onRowClick = {},
      onCurrencyClick = {},
      isCurrencySelectable = true,
      isActive = true,
    )
  }
}
