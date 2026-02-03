package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.astamato.exchangeratecalculatorapp.ui.util.Currency
import com.astamato.exchangeratecalculatorapp.ui.util.CurrencyUtils
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ExchangeRateUiState
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.MainViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CurrencyExchangeScreen(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        Text(
            text = "Exchange",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is ExchangeRateUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ExchangeRateUiState.Success -> {
                    CurrencyExchangeContent(
                        state = state,
                        onKeypadPress = viewModel::onKeypadPress,
                        onCurrencySelected = viewModel::onCurrencySelected,
                        onSwapCurrencies = viewModel::onSwapCurrencies,
                        onActiveFieldChange = viewModel::onActiveFieldChange
                    )
                }

                is ExchangeRateUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyExchangeContent(
    state: ExchangeRateUiState.Success,
    onKeypadPress: (String) -> Unit,
    onCurrencySelected: (String) -> Unit,
    onSwapCurrencies: () -> Unit,
    onActiveFieldChange: (Int) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val (primaryCurrency, secondaryCurrency) = if (state.isUsdcPrimary) {
        CurrencyUtils.getCurrency("USDc")!! to CurrencyUtils.getCurrency(state.selectedCurrency)!!
    } else {
        CurrencyUtils.getCurrency(state.selectedCurrency)!! to CurrencyUtils.getCurrency("USDc")!!
    }

    val selectedTicker = state.tickers.find { it.book.endsWith(state.selectedCurrency.lowercase()) }
    val exchangeRate = selectedTicker?.ask?.toBigDecimal() ?: BigDecimal.ONE
    val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
        maximumFractionDigits = 2
    }
    val formattedExchangeRate = numberFormat.format(exchangeRate)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Exchange calculator", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "1 USDc = $formattedExchangeRate ${state.selectedCurrency}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(contentAlignment = Alignment.Center) {
            Column {
                CurrencyRow(
                    currency = primaryCurrency,
                    amount = state.amount1,
                    onRowClick = { onActiveFieldChange(1) },
                    onCurrencyClick = { if (!state.isUsdcPrimary) showBottomSheet = true },
                    isCurrencySelectable = !state.isUsdcPrimary,
                    isActive = state.activeField == 1
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                CurrencyRow(
                    currency = secondaryCurrency,
                    amount = state.amount2,
                    onRowClick = { onActiveFieldChange(2) },
                    onCurrencyClick = { if (state.isUsdcPrimary) showBottomSheet = true },
                    isCurrencySelectable = state.isUsdcPrimary,
                    isActive = state.activeField == 2
                )
            }
            IconButton(
                onClick = onSwapCurrencies,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap currencies",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        NumericKeypad(onKeyPress = onKeypadPress)

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                ChooseCurrencySheet(
                    availableCurrencies = state.availableCurrencies,
                    selectedCurrency = state.selectedCurrency,
                    onCurrencySelected = {
                        onCurrencySelected(it)
                        showBottomSheet = false
                    },
                    onClose = { showBottomSheet = false }
                )
            }
        }
    }
}

@Composable
fun CurrencyRow(
    currency: Currency,
    amount: String,
    onRowClick: () -> Unit,
    onCurrencyClick: () -> Unit,
    isCurrencySelectable: Boolean,
    isActive: Boolean
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
        maximumFractionDigits = 2
    }
    val formattedAmount = amount.toBigDecimalOrNull()?.let { numberFormat.format(it) } ?: amount

    val backgroundColor = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onRowClick)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable(enabled = isCurrencySelectable, onClick = onCurrencyClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = currency.flag, fontSize = 24.sp)
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Text(text = currency.code, fontWeight = FontWeight.Bold)
            if (isCurrencySelectable) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select currency")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$${formattedAmount}",
            fontSize = 22.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Light,
            letterSpacing = 1.1.sp
        )
    }
}

@Composable
fun ChooseCurrencySheet(
    availableCurrencies: List<String>,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Choose currency", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = onClose) {
                Text(text = "X", fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        availableCurrencies.forEach { currencyCode ->
            val currency = CurrencyUtils.getCurrency(currencyCode)
            if (currency != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCurrencySelected(currencyCode) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = currency.flag, fontSize = 24.sp)
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                    Text(text = currency.name, modifier = Modifier.weight(1f))
                    if (currencyCode == selectedCurrency) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
