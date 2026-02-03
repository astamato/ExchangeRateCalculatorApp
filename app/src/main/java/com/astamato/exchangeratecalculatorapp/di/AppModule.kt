package com.astamato.exchangeratecalculatorapp.di

import com.astamato.exchangeratecalculatorapp.network.ExchangeRateApiService
import com.astamato.exchangeratecalculatorapp.network.interceptor.EmptyListOnErrorInterceptor
import com.astamato.exchangeratecalculatorapp.repository.ExchangeRateRepository
import com.astamato.exchangeratecalculatorapp.repository.ExchangeRateRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(EmptyListOnErrorInterceptor())
                .build()
        }

        @Provides
        @Singleton
        fun provideExchangeRateApi(okHttpClient: OkHttpClient): ExchangeRateApiService {
            val json = Json {
                ignoreUnknownKeys = true
            }
            return Retrofit
                .Builder()
                .baseUrl("https://api.dolarapp.dev/")
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(ExchangeRateApiService::class.java)
        }
    }
}
