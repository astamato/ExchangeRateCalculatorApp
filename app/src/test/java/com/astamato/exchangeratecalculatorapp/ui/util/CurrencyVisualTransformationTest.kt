package com.astamato.exchangeratecalculatorapp.ui.util

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyVisualTransformationTest {

  private val transformation = CurrencyVisualTransformation()

  private fun transform(input: String): String {
    return transformation.filter(AnnotatedString(input)).text.text
  }

  private fun originalToTransformed(input: String, offset: Int): Int {
    return transformation.filter(AnnotatedString(input)).offsetMapping.originalToTransformed(offset)
  }

  private fun transformedToOriginal(input: String, offset: Int): Int {
    return transformation.filter(AnnotatedString(input)).offsetMapping.transformedToOriginal(offset)
  }

  @Test
  fun `empty string produces dollar sign only`() {
    assertEquals("$", transform(""))
  }

  @Test
  fun `single digit has no comma`() {
    assertEquals("$5", transform("5"))
  }

  @Test
  fun `three digits have no comma`() {
    assertEquals("$999", transform("999"))
  }

  @Test
  fun `four digits get one comma`() {
    assertEquals("$1,000", transform("1000"))
  }

  @Test
  fun `six digits get one comma`() {
    assertEquals("$184,065", transform("184065"))
  }

  @Test
  fun `seven digits get two commas`() {
    assertEquals("$1,234,567", transform("1234567"))
  }

  @Test
  fun `amount with decimals`() {
    assertEquals("$184,065.59", transform("184065.59"))
  }

  @Test
  fun `small amount with decimals`() {
    assertEquals("$1.50", transform("1.50"))
  }

  @Test
  fun `zero`() {
    assertEquals("$0", transform("0"))
  }

  @Test
  fun `zero with decimals`() {
    assertEquals("$0.99", transform("0.99"))
  }

  @Test
  fun `large number with decimals`() {
    assertEquals("$1,234,567.89", transform("1234567.89"))
  }

  @Test
  fun `originalToTransformed maps correctly for 184065 dot 59`() {
    val input = "184065.59"
    assertEquals(1, originalToTransformed(input, 0))  // '1' -> after '$'
    assertEquals(2, originalToTransformed(input, 1))  // '8'
    assertEquals(3, originalToTransformed(input, 2))  // '4'
    assertEquals(5, originalToTransformed(input, 3))  // '0' -> after comma
    assertEquals(6, originalToTransformed(input, 4))  // '6'
    assertEquals(7, originalToTransformed(input, 5))  // '5'
    assertEquals(8, originalToTransformed(input, 6))  // '.'
    assertEquals(9, originalToTransformed(input, 7))  // '5'
    assertEquals(10, originalToTransformed(input, 8)) // '9'
    assertEquals(11, originalToTransformed(input, 9)) // end
  }

  @Test
  fun `transformedToOriginal maps correctly for 184065 dot 59`() {
    val input = "184065.59"
    assertEquals(0, transformedToOriginal(input, 0))  // before '$'
    assertEquals(0, transformedToOriginal(input, 1))  // '$' or '1'
    assertEquals(1, transformedToOriginal(input, 2))  // '8'
    assertEquals(2, transformedToOriginal(input, 3))  // '4'
    assertEquals(3, transformedToOriginal(input, 4))  // comma -> maps to '0'
    assertEquals(3, transformedToOriginal(input, 5))  // '0'
    assertEquals(4, transformedToOriginal(input, 6))  // '6'
    assertEquals(5, transformedToOriginal(input, 7))  // '5'
    assertEquals(6, transformedToOriginal(input, 8))  // '.'
    assertEquals(7, transformedToOriginal(input, 9))  // '5'
    assertEquals(8, transformedToOriginal(input, 10)) // '9'
    assertEquals(9, transformedToOriginal(input, 11)) // end
  }

  @Test
  fun `offset mapping roundtrip for simple number`() {
    val input = "1000"
    for (i in 0..input.length) {
      val transformed = originalToTransformed(input, i)
      val back = transformedToOriginal(input, transformed)
      assertEquals(i, back)
    }
  }
}
