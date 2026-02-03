package com.astamato.exchangeratecalculatorapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Ticker(
    val ask: String,
    val bid: String,
    val book: String,
    val date: String
)