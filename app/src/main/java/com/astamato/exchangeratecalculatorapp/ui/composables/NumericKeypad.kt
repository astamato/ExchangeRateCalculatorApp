package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme

@Composable
fun NumericKeypad(
  onKeyPress: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val keypadLayout =
    remember {
      listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "<"),
      )
    }

  Column(modifier = modifier.padding(16.dp)) {
    keypadLayout.forEach { row ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
      ) {
        row.forEach { key ->
          Button(onClick = { onKeyPress(key) }) {
            Text(text = key)
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun NumericKeypadPreview() {
  ExchangeRateCalculatorAppTheme {
    NumericKeypad(onKeyPress = {})
  }
}
