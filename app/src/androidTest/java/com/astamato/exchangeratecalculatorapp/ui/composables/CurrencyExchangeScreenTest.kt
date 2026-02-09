package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.astamato.exchangeratecalculatorapp.data.Ticker
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ActiveField
import com.astamato.exchangeratecalculatorapp.ui.viewmodel.ExchangeRateUiState
import org.junit.Rule
import org.junit.Test

class CurrencyExchangeScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun currencyExchangeScreen_showsSuccessContent() {
    val uiState = ExchangeRateUiState.Success(
      tickers = listOf(
        Ticker(ask = "18.4065", bid = "18.40", book = "usdc_mxn", date = "")
      ),
      availableCurrencies = listOf("MXN", "ARS"),
      selectedCurrency = "MXN",
      amountPrimary = "100",
      amountSecondary = "1840.65",
      isUsdcPrimary = true,
      activeField = ActiveField.PRIMARY
    )

    composeTestRule.setContent {
      ExchangeRateCalculatorAppTheme {
        StatelessCurrencyExchangeScreen(
          uiState = uiState,
          onCurrencySelected = {},
          onSwapCurrencies = {},
          onActiveFieldChange = {},
          onAmount1Change = {},
          onAmount2Change = {},
        )
      }
    }

    // Header
    composeTestRule.onNodeWithText("Exchange calculator").assertIsDisplayed()
    // 18.4065 should be rounded to 18.41 in the header based on NumberFormat.maximumFractionDigits = 2
    composeTestRule.onNodeWithText("1 USDc = 18.41 MXN").assertIsDisplayed()

    // Input rows
    composeTestRule.onNodeWithText("USDc").assertIsDisplayed()
    composeTestRule.onNodeWithText("MXN").assertIsDisplayed()

    // CurrencyVisualTransformation adds a "$" prefix and commas for thousands
    composeTestRule.onNodeWithText("$100").assertIsDisplayed()
    composeTestRule.onNodeWithText("$1,840.65").assertIsDisplayed()
  }

  @Test
  fun swapButton_triggersCallback() {
    var swapClicked = false
    val uiState = ExchangeRateUiState.Success(
      tickers = emptyList(),
      availableCurrencies = listOf("MXN"),
      selectedCurrency = "MXN",
      amountPrimary = "100",
      amountSecondary = "1840.65",
    )

    composeTestRule.setContent {
      ExchangeRateCalculatorAppTheme {
        StatelessCurrencyExchangeScreen(
          uiState = uiState,
          onCurrencySelected = {},
          onSwapCurrencies = { swapClicked = true },
          onActiveFieldChange = {},
          onAmount1Change = {},
          onAmount2Change = {},
        )
      }
    }

    composeTestRule.onNodeWithContentDescription("Swap currencies").performClick()
    assert(swapClicked)
  }

  @Test
  fun clickingCurrency_opensBottomSheet() {
    val uiState = ExchangeRateUiState.Success(
      tickers = emptyList(),
      availableCurrencies = listOf("MXN", "ARS"),
      selectedCurrency = "MXN",
      amountPrimary = "100",
      amountSecondary = "1840.65",
      isUsdcPrimary = true,
    )

    composeTestRule.setContent {
      ExchangeRateCalculatorAppTheme {
        StatelessCurrencyExchangeScreen(
          uiState = uiState,
          onCurrencySelected = {},
          onSwapCurrencies = {},
          onActiveFieldChange = {},
          onAmount1Change = {},
          onAmount2Change = {},
        )
      }
    }

    // Click on the selectable currency row
    composeTestRule.onNodeWithContentDescription("Select currency").performClick()

    // Check if bottom sheet title is displayed
    composeTestRule.onNodeWithText("Choose currency").assertIsDisplayed()
    composeTestRule.onNodeWithText("ARS").assertIsDisplayed()
  }

  @Test
  fun selectingCurrencyFromSheet_triggersCallback() {
    var selectedCurrency: String? = null
    val uiState = ExchangeRateUiState.Success(
      tickers = emptyList(),
      availableCurrencies = listOf("MXN", "ARS"),
      selectedCurrency = "MXN",
      amountPrimary = "100",
      amountSecondary = "1840.65",
      isUsdcPrimary = true,
    )

    composeTestRule.setContent {
      ExchangeRateCalculatorAppTheme {
        StatelessCurrencyExchangeScreen(
          uiState = uiState,
          onCurrencySelected = { selectedCurrency = it },
          onSwapCurrencies = {},
          onActiveFieldChange = {},
          onAmount1Change = {},
          onAmount2Change = {},
        )
      }
    }

    // Open sheet
    composeTestRule.onNodeWithContentDescription("Select currency").performClick()

    // Select ARS. We use the Text node "ARS".
    // Note: The Image also has contentDescription "ARS", so we might need to be specific.
    composeTestRule.onNodeWithText("ARS").performClick()

    assert(selectedCurrency == "ARS")
  }

  @Test
  fun enteringAmountInPrimaryField_triggersCallback() {
    var amountChanged: String? = null
    val uiState = ExchangeRateUiState.Success(
      tickers = emptyList(),
      availableCurrencies = listOf("MXN"),
      selectedCurrency = "MXN",
      amountPrimary = "100",
      amountSecondary = "1840.65",
      activeField = ActiveField.PRIMARY,
      isUsdcPrimary = true
    )

    composeTestRule.setContent {
      ExchangeRateCalculatorAppTheme {
        StatelessCurrencyExchangeScreen(
          uiState = uiState,
          onCurrencySelected = {},
          onSwapCurrencies = {},
          onActiveFieldChange = {},
          onAmount1Change = { amountChanged = it },
          onAmount2Change = {},
        )
      }
    }

    // Find the text field with "$100" and type "200"
    composeTestRule.onNodeWithText("$100").performTextInput("200")

    assert(amountChanged != null)
  }

  @Test
  fun focusingSecondaryField_triggersCallback() {
    var activeField: ActiveField? = null
    val uiState = ExchangeRateUiState.Success(
      tickers = emptyList(),
      availableCurrencies = listOf("MXN"),
      selectedCurrency = "MXN",
      amountPrimary = "100",
      amountSecondary = "1840.65",
      activeField = ActiveField.PRIMARY,
      isUsdcPrimary = true
    )

    composeTestRule.setContent {
      ExchangeRateCalculatorAppTheme {
        StatelessCurrencyExchangeScreen(
          uiState = uiState,
          onCurrencySelected = {},
          onSwapCurrencies = {},
          onActiveFieldChange = { activeField = it },
          onAmount1Change = {},
          onAmount2Change = {},
        )
      }
    }

    // Click on the secondary amount field to trigger focus change
    composeTestRule.onNodeWithText("$1,840.65").performClick()

    // Wait for the UI to settle if necessary, though performClick should be enough
    composeTestRule.waitForIdle()

    assert(activeField == ActiveField.SECONDARY)
  }

  @Test
  fun currencyExchangeScreen_showsError() {
    val errorMessage = "Test Error"
    composeTestRule.setContent {
      ExchangeRateCalculatorAppTheme {
        StatelessCurrencyExchangeScreen(
          uiState = ExchangeRateUiState.Error(errorMessage),
          onCurrencySelected = {},
          onSwapCurrencies = {},
          onActiveFieldChange = {},
          onAmount1Change = {},
          onAmount2Change = {},
        )
      }
    }

    composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
  }
}
