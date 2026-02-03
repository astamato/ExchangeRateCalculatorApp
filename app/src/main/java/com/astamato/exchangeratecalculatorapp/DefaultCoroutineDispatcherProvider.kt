package com.astamato.askgranola.common.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DefaultCoroutineDispatcherProvider @Inject constructor() : CoroutineDispatcherProvider {
  override val main: CoroutineDispatcher = Dispatchers.Main
  override val io: CoroutineDispatcher = Dispatchers.IO
  override val default: CoroutineDispatcher = Dispatchers.Default
}
