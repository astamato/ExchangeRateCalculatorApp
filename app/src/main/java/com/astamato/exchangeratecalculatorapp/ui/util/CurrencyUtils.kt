package com.astamato.exchangeratecalculatorapp.ui.util

data class Currency(
    val code: String,
    val name: String,
    val flag: String,
)

object CurrencyUtils {
    val currencies =
        listOf(
            Currency("USDc", "USD Coin", "\ud83c\uddfa\ud83c\uddf8"),
            Currency("ARS", "Argentine Peso", "\ud83c\udde6\ud83c\uddf7"),
            Currency("COP", "Colombian Peso", "\ud83c\udde8\ud83c\uddf4"),
            Currency("MXN", "Mexican Peso", "\ud83c\uddf2\ud83c\uddfd"),
            Currency("BRL", "Brazilian Real", "\ud83c\udde7\ud83c\uddf7"),
        )

    fun getCurrency(code: String): Currency? = currencies.find { it.code == code }
}
