package com.astamato.exchangeratecalculatorapp.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyVisualTransformation : VisualTransformation {
  override fun filter(text: AnnotatedString): TransformedText {
    val originalText = text.text
    val dotIndex = originalText.indexOf('.')
    val integerPart = if (dotIndex >= 0) originalText.substring(0, dotIndex) else originalText
    val decimalPart = if (dotIndex >= 0) originalText.substring(dotIndex) else ""

    val formatted = StringBuilder("$")
    val originalToTransformedOffsets = IntArray(originalText.length + 1)
    var transformedIndex = 1

    integerPart.forEachIndexed { index, c ->
      if (index > 0 && (integerPart.length - index) % 3 == 0) {
        formatted.append(',')
        transformedIndex++
      }
      originalToTransformedOffsets[index] = transformedIndex
      formatted.append(c)
      transformedIndex++
    }

    decimalPart.forEachIndexed { index, c ->
      originalToTransformedOffsets[integerPart.length + index] = transformedIndex
      formatted.append(c)
      transformedIndex++
    }

    originalToTransformedOffsets[originalText.length] = transformedIndex

    val offsetMapping =
      object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
          originalToTransformedOffsets[offset.coerceIn(0, originalText.length)]

        override fun transformedToOriginal(offset: Int): Int {
          if (offset <= 0) return 0
          for (i in originalToTransformedOffsets.indices) {
            if (originalToTransformedOffsets[i] >= offset) return i
          }
          return originalText.length
        }
      }

    return TransformedText(AnnotatedString(formatted.toString()), offsetMapping)
  }
}
