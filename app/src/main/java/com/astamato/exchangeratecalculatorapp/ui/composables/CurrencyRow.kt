package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.astamato.exchangeratecalculatorapp.R
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme
import com.astamato.exchangeratecalculatorapp.ui.util.Currency
import com.astamato.exchangeratecalculatorapp.ui.util.CurrencyUtils

private const val MAX_CHARACTERS = 9

private class CurrencyVisualTransformation : VisualTransformation {
  override fun filter(text: AnnotatedString): TransformedText {
    val transformedText = AnnotatedString("$${text.text}")
    val offsetMapping =
      object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = offset + 1

        override fun transformedToOriginal(offset: Int): Int = maxOf(0, offset - 1)
      }
    return TransformedText(transformedText, offsetMapping)
  }
}

@Composable
fun CurrencyRow(
  currency: Currency,
  amount: String,
  onAmountChange: (String) -> Unit,
  onCurrencyClick: () -> Unit,
  onFocusChanged: (Boolean) -> Unit,
  isCurrencySelectable: Boolean,
  isActive: Boolean,
  modifier: Modifier = Modifier,
) {
  val focusRequester = remember { FocusRequester() }
  var textFieldValue by remember(amount) {
    mutableStateOf(TextFieldValue(text = amount, selection = TextRange(amount.length)))
  }

  LaunchedEffect(amount) {
    if (textFieldValue.text != amount) {
      textFieldValue = TextFieldValue(text = amount, selection = TextRange(amount.length))
    }
  }

  LaunchedEffect(isActive) {
    if (isActive) {
      focusRequester.requestFocus()
    }
  }

  Row(
    modifier =
      modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.surface)
        .padding(vertical = 16.dp, horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      modifier = Modifier.clickable(enabled = isCurrencySelectable, onClick = onCurrencyClick),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        painter = painterResource(id = currency.flag),
        contentDescription = currency.code,
        modifier = Modifier.size(16.dp),
      )
      Spacer(modifier = Modifier.padding(start = 16.dp))
      Text(text = currency.code, style = MaterialTheme.typography.titleMedium)
      Spacer(modifier = Modifier.padding(start = 8.dp))
      if (isCurrencySelectable) {
        Icon(
          painter = painterResource(id = R.drawable.chevron_button),
          contentDescription = "Select currency",
        )
      }
    }
    Spacer(modifier = Modifier.weight(1f))
    BasicTextField(
      value = textFieldValue,
      onValueChange = { newValue ->
        val filtered =
          newValue.text
            .filter { it.isDigit() || it == '.' }
            .let { text ->
              // Remove leading zeros unless it's "0" or "0."
              when {
                text.isEmpty() -> ""
                text == "0" || text.startsWith("0.") -> text
                text.startsWith("0") -> text.trimStart('0').ifEmpty { "0" }
                else -> text
              }
            }
            .let { text ->
              // Limit decimal places to 2
              val dotIndex = text.indexOf('.')
              if (dotIndex >= 0 && text.length > dotIndex + 3) {
                text.substring(0, dotIndex + 3)
              } else {
                text
              }
            }

        if (filtered.count { it == '.' } <= 1 && filtered.length <= MAX_CHARACTERS) {
          textFieldValue = newValue.copy(text = filtered, selection = TextRange(filtered.length))
          onAmountChange(filtered)
        }
      },
      modifier =
        Modifier.focusRequester(focusRequester)
          .onFocusChanged { focusState -> onFocusChanged(focusState.isFocused) }
          .widthIn(min = 50.dp, max = 150.dp),
      textStyle =
        MaterialTheme.typography.bodyLarge.copy(
          fontWeight = if (isActive) FontWeight.Bold else FontWeight.Light,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.End,
        ),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
      singleLine = true,
      cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
      visualTransformation = CurrencyVisualTransformation(),
    )
  }
}

@PreviewLightDark
@Composable
fun CurrencyRowPreview() {
  ExchangeRateCalculatorAppTheme {
    CurrencyRow(
      currency = CurrencyUtils.getCurrency("MXN")!!,
      amount = "184065.59",
      onAmountChange = {},
      onCurrencyClick = {},
      onFocusChanged = {},
      isCurrencySelectable = true,
      isActive = true,
    )
  }
}
