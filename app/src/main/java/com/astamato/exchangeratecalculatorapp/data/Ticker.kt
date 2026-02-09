package com.astamato.exchangeratecalculatorapp.data

import kotlinx.serialization.Serializable

/**
 * Represents an exchange rate ticker from the DolarApp API.
 *
 * A ticker contains the current bid/ask prices for a currency pair (book).
 *
 * Example API response:
 * ```json
 * {
 *   "ask": "17.45",
 *   "bid": "17.40",
 *   "book": "usdc_mxn",
 *   "date": "2024-01-15T12:00:00Z"
 * }
 * ```
 *
 * @property ask The asking price - the price at which you can BUY the base currency (USDC).
 *               The app uses this value when converting from Local currency → USDC.
 * @property bid The bid price - the price at which you can SELL the base currency (USDC).
 *               The app uses this value when converting from USDC → Local currency.
 * @property book The trading pair identifier in format "base_quote" (e.g., "usdc_mxn", "usdc_ars").
 *                The base currency is USDC, the quote currency is the local currency (MXN, ARS, BRL, COP).
 * @property date ISO 8601 timestamp of when this ticker was last updated.
 */
@Serializable
data class Ticker(
  val ask: String,
  val bid: String,
  val book: String,
  val date: String,
)
