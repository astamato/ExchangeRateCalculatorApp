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
        Ticker(ask = "18.5", bid = "18.0", book = "usdc_mxn", date = "2021-09-01T00:00:00+00:00"),
        Ticker(ask = "1.0", bid = "1.0", book = "usdc_usd", date = "2021-09-01T00:00:00+00:00")
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
        // Initial state is USDC -> Local. Should use bid rate (18.0).
        assertEquals("18", (state as ExchangeRateUiState.Success).amountSecondary)
      }
    }

  @Test
  fun `GIVEN success state WHEN swap currencies THEN values recalculate with spread`() =
    runTest {
      // GIVEN
      val currencies = listOf("MXN")
      val tickers = listOf(
        Ticker(ask = "20.0", bid = "18.0", book = "usdc_mxn", date = "2021-09-01T00:00:00+00:00")
      )
      coEvery { repository.getAvailableCurrencies() } returns currencies
      coEvery { repository.getTickers(any()) } returns tickers
      viewModel = MainViewModel(repository, logger)

      viewModel.uiState.test {
        awaitItem() // Loading
        val initialState = awaitItem() as ExchangeRateUiState.Success
        assertEquals("1", initialState.amountPrimary) // 1 USDC
        assertEquals("18", initialState.amountSecondary) // 18 MXN (bid)

        // WHEN
        viewModel.onSwapCurrencies()

        // THEN
        val swappedState = awaitItem() as ExchangeRateUiState.Success
        assertEquals(false, swappedState.isUsdcPrimary)
        assertEquals("18", swappedState.amountPrimary) // 18 MXN on top
        // Should recalculate bottom (USDC) using ask: 18 / 20.0 = 0.9
        assertEquals("0.9", swappedState.amountSecondary)
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
