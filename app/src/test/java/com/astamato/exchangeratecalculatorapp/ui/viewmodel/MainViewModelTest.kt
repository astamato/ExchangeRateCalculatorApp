package com.astamato.exchangeratecalculatorapp.ui.viewmodel

import app.cash.turbine.test
import com.astamato.exchangeratecalculatorapp.data.Ticker
import com.astamato.exchangeratecalculatorapp.repository.ExchangeRateRepository
import com.astamato.exchangeratecalculatorapp.util.Logger
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
  private val testDispatcher = StandardTestDispatcher()

  private lateinit var repository: ExchangeRateRepository
  private lateinit var logger: Logger
  private lateinit var viewModel: MainViewModel

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    repository = mockk()
    logger = mockk(relaxed = true)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `GIVEN repository returns success WHEN viewmodel is created THEN uiState is Success`() =
    runTest {
      // GIVEN
      val currencies = listOf("MXN", "USD")
      val tickers = listOf(
        Ticker(ask = "1000000", bid = "990000", book = "usdc_mxn", date = "2021-09-01T00:00:00+00:00"),
        Ticker(ask = "50000", bid = "49000", book = "usdc_usd", date = "2021-09-01T00:00:00+00:00")
      )
      coEvery { repository.getAvailableCurrencies() } returns currencies
      coEvery { repository.getTickers(any()) } returns tickers

      // WHEN
      viewModel = MainViewModel(repository, logger)

      // THEN
      viewModel.uiState.test {
        assertEquals(ExchangeRateUiState.Loading, awaitItem())
        val state = awaitItem()
        assert(state is ExchangeRateUiState.Success)
        assertEquals("1000000", (state as ExchangeRateUiState.Success).amountSecondary)
      }
    }

  @Test
  fun `GIVEN repository throws exception WHEN viewmodel is created THEN uiState is Error`() =
    runTest {
      // GIVEN
      coEvery { repository.getAvailableCurrencies() } throws Exception()

      // WHEN
      viewModel = MainViewModel(repository, logger)

      // THEN
      viewModel.uiState.test {
        assertEquals(ExchangeRateUiState.Loading, awaitItem())
        assert(awaitItem() is ExchangeRateUiState.Error)
      }
    }
}
