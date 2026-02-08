package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme
import com.astamato.exchangeratecalculatorapp.ui.util.CurrencyUtils

@Composable
fun CurrencyFlag(
  @DrawableRes flagResId: Int,
  contentDescription: String?,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .size(40.dp)
      .clip(RoundedCornerShape(10.dp))
      .background(Color(0xFFF4F4F4)),
    contentAlignment = Alignment.Center,
  ) {
    Image(
      painter = painterResource(id = flagResId),
      contentDescription = contentDescription,
      modifier = Modifier.size(28.dp),
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun CurrencyFlagPreview() {
  val currency = CurrencyUtils.getCurrency("MXN")
  if (currency != null) {
    ExchangeRateCalculatorAppTheme {
      CurrencyFlag(
        flagResId = currency.flag,
        contentDescription = currency.name,
      )
    }
  }
}
