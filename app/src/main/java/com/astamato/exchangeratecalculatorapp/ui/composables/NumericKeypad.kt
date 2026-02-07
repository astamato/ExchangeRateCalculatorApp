package com.astamato.exchangeratecalculatorapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment.Companion.Top
import androidx.compose.ui.text.style.LineHeightStyle.Mode.Companion.Tight
import androidx.compose.ui.text.style.LineHeightStyle.Trim.Companion.Both
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
      .background(color = Color(0xFF535353))
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
      .height(46.dp)
      .clip(RoundedCornerShape(5.dp))
      .background(color = Color(0xFFFCFCFE))
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center,
  ) {
    when (key.number) {
      "<" -> {
        Image(
          painter = painterResource(id = R.drawable.symbol),
          contentDescription = "Backspace",
          modifier = Modifier.size(28.dp),
        )
      }

      else -> {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            style = LocalTextStyle.current.merge(
              TextStyle(
                lineHeightStyle = LineHeightStyle(
                  alignment = LineHeightStyle.Alignment.Center,
                  trim = Both,
                  )
              )
            ),
            text = key.number,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          if (letters != null) {
            Text(
              style = LocalTextStyle.current.merge(
                TextStyle(
                  lineHeightStyle = LineHeightStyle(
                    alignment = Top,
                    trim = Both,
                  )
                )
              ),
              text = letters,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
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
