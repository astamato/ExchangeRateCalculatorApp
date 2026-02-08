package com.astamato.exchangeratecalculatorapp.ui.util

import androidx.annotation.DrawableRes
import com.astamato.exchangeratecalculatorapp.R

data class Currency(
  val code: String,
  @DrawableRes val flag: Int,
)

object CurrencyUtils {
  val currencies =
    listOf(
      Currency("USDc", R.drawable.logo_us),
      Currency("ARS", R.drawable.logo_ar),
      Currency("COP", R.drawable.logo_co),
      Currency("MXN", R.drawable.logo_mx),
      Currency("BRL", R.drawable.logo_br),
    )

  fun getCurrency(code: String): Currency? = currencies.find { it.code == code }

  fun getAvailableCurrencyCodes(): List<String> = currencies.map { it.code }
}
