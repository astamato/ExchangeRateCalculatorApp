package com.astamato.exchangeratecalculatorapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.astamato.exchangeratecalculatorapp.R
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ActiveField
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ExchangeRateUiState
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateScreen(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsState()

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
      )
    },
  ) {
    Column(modifier = Modifier.padding(it)) {
      when (val state = uiState) {
        is ExchangeRateUiState.Loading -> {
          // TODO: Add loading indicator
        }

        is ExchangeRateUiState.Success -> {
          AmountInput(state = state, onAmountChange = { amount ->
            if (state.activeField == ActiveField.PRIMARY) {
              viewModel.onPrimaryAmountChange(amount)
            } else {
              viewModel.onSecondaryAmountChange(amount)
            }
          }, onCurrencySelected = { currency ->
            viewModel.onCurrencySelected(currency)
          }, onActiveFieldChange = { field -> viewModel.onActiveFieldChange(field) })
        }

        is ExchangeRateUiState.Error -> {
          Text(text = state.message)
        }
      }
    }
  }
}
