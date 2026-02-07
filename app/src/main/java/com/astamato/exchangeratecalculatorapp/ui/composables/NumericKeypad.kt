package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.astamato.exchangeratecalculatorapp.R
import com.astamato.exchangeratecalculatorapp.ui.theme.ExchangeRateCalculatorAppTheme

private data class KeypadKey(val number: String, val letters: String? = null)

private val keypadKeys = listOf(
  KeypadKey("1", ""),
  KeypadKey("2", "ABC"),
  KeypadKey("3", "DEF"),
  KeypadKey("4", "GHI"),
  KeypadKey("5", "JKL"),
  KeypadKey("6", "MNO"),
  KeypadKey("7", "PQRS"),
  KeypadKey("8", "TUV"),
  KeypadKey("9", "WXYZ"),
  KeypadKey("."),
  KeypadKey("0"),
  KeypadKey("<"),
)

@Composable
fun NumericKeypad(
  onKeyPress: (String) -> Unit,
  modifier: Modifier = Modifier,
) {

  LazyVerticalGrid(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(horizontal = 4.dp, vertical = 4.dp),
    columns = GridCells.Fixed(3),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    items(keypadKeys) { key ->
      KeypadButton(
        key = key,
        onClick = { onKeyPress(key.number) },
      )
    }
  }
}

@Composable
private fun KeypadButton(
  key: KeypadKey,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val letters = key.letters

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(5.dp))
      .background(MaterialTheme.colorScheme.onSurfaceVariant)
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center,
  ) {
    when (key.number) {
      "<" -> {
        Image(
          painter = painterResource(id = R.drawable.symbol),
          contentDescription = "Backspace",
          modifier = Modifier.size(28.dp),
          colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
        )
      }

      else -> {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = key.number,
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          if (letters != null) {
            Text(
              text = letters,
              fontSize = 10.sp,
              fontWeight = FontWeight.Normal,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
              textAlign = TextAlign.Center,
              letterSpacing = 1.sp,
            )
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
