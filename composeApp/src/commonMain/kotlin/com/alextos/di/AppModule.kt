package com.alextos.di

import com.alextos.core.data.HttpClientFactory
import com.alextos.data.CurrencyRepositoryImpl
import com.alextos.data.network.KtorRemoteCurrencyDataSource
import com.alextos.data.network.RemoteCurrencyDataSource
import com.alextos.domain.repository.CurrencyRepository
import com.alextos.presentation.scenes.MainViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    factory<HttpClient> { HttpClientFactory.create(get()) }
    factory<RemoteCurrencyDataSource> { KtorRemoteCurrencyDataSource(get()) }
    factory<CurrencyRepository> { CurrencyRepositoryImpl(get()) }
    viewModelOf(::MainViewModel)
}