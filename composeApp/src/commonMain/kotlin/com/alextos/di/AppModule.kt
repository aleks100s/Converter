package com.alextos.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.alextos.common.data.HttpClientFactory
import com.alextos.converter.data.CurrencyRepositoryImpl
import com.alextos.converter.data.database.AppDatabase
import com.alextos.converter.data.database.DatabaseFactory
import com.alextos.converter.data.database.DatabaseSeeder
import com.alextos.converter.data.network.KtorRemoteCurrencyDataSource
import com.alextos.converter.data.network.RemoteCurrencyDataSource
import com.alextos.converter.domain.repository.CurrencyRepository
import com.alextos.converter.presentation.scenes.MainViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    single<HttpClient> { HttpClientFactory.create(get()) }
    single<RemoteCurrencyDataSource> { KtorRemoteCurrencyDataSource(get()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get(), get()) }

    single {
        get<DatabaseFactory>().create()
            .addCallback(DatabaseSeeder())
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<AppDatabase>().currencyRateDao() }

    viewModelOf(::MainViewModel)
}