package com.astamato.exchangeratecalculatorapp.ui.util

import androidx.annotation.DrawableRes
import com.astamato.exchangeratecalculatorapp.R

data class Currency(
  val code: String,
  val name: String,
  @DrawableRes val flag: Int,
)

object CurrencyUtils {
  val currencies =
    listOf(
      Currency("USDc", "USD Coin", R.drawable.logo_us),
      Currency("ARS", "Argentine Peso", R.drawable.logo_ar),
      Currency("COP", "Colombian Peso", R.drawable.logo_co),
      Currency("MXN", "Mexican Peso", R.drawable.logo_mx),
      Currency("BRL", "Brazilian Real", R.drawable.logo_br),
    )

  fun getCurrency(code: String): Currency? = currencies.find { it.code == code }
}
