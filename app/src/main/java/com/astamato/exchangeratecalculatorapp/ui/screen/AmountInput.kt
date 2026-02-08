package com.astamato.exchangeratecalculatorapp.ui.screen

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ActiveField
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ExchangeRateUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountInput(
  state: ExchangeRateUiState.Success,
  onAmountChange: (String) -> Unit,
  onCurrencySelected: (String) -> Unit,
  onActiveFieldChange: (ActiveField) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }

  TextField(
    value = state.amountPrimary,
    onValueChange = {
      onActiveFieldChange(ActiveField.PRIMARY)
      onAmountChange(it)
    },
    label = { Text(if (state.isUsdcPrimary) "USDC" else state.selectedCurrency) },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
  )

  TextField(
    value = state.amountSecondary,
    onValueChange = {
      onActiveFieldChange(ActiveField.SECONDARY)
      onAmountChange(it)
    },
    label = { Text(if (state.isUsdcPrimary) state.selectedCurrency else "USDC") },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
  )

  ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
    TextField(
      value = state.selectedCurrency,
      onValueChange = {},
      readOnly = true,
      trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
      modifier = androidx.compose.ui.Modifier.menuAnchor(),
    )

    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      state.availableCurrencies.forEach { currency ->
        DropdownMenuItem(
          text = { Text(currency) },
          onClick = {
            onCurrencySelected(currency)
            expanded = false
          },
        )
      }
    }
  }
}
