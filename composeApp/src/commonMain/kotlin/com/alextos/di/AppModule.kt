package com.alextos.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.alextos.core.data.HttpClientFactory
import com.alextos.data.CurrencyRepositoryImpl
import com.alextos.data.database.AppDatabase
import com.alextos.data.database.DatabaseFactory
import com.alextos.data.network.KtorRemoteCurrencyDataSource
import com.alextos.data.network.RemoteCurrencyDataSource
import com.alextos.domain.repository.CurrencyRepository
import com.alextos.presentation.scenes.MainViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    single<HttpClient> { HttpClientFactory.create(get()) }
    single<RemoteCurrencyDataSource> { KtorRemoteCurrencyDataSource(get()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<AppDatabase>().currencyRateDao()}

    viewModelOf(::MainViewModel)
}