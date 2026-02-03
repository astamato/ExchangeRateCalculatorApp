package com.astamato.askgranola.common.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
  val main: CoroutineDispatcher
  val io: CoroutineDispatcher
  val default: CoroutineDispatcher
}
