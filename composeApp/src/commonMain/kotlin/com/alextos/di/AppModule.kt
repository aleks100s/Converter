package com.alextos.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.alextos.app.navigation.ApplicationViewModel
import com.alextos.common.data.HttpClientFactory
import com.alextos.converter.data.CurrencyRepositoryImpl
import com.alextos.converter.data.database.AppDatabase
import com.alextos.converter.data.database.DatabaseFactory
import com.alextos.converter.data.database.DatabaseSeeder
import com.alextos.converter.data.network.KtorRemoteCurrencyDataSource
import com.alextos.converter.data.network.RemoteCurrencyDataSource
import com.alextos.converter.data.storage.StorageServiceImpl
import com.alextos.converter.domain.repository.CurrencyRepository
import com.alextos.converter.domain.camera.ConverterAppDelegate
import com.alextos.converter.domain.camera.ConverterUseCase
import com.alextos.converter.domain.favourites.GetFavouriteCurrencyRatesUseCase
import com.alextos.converter.domain.storage.StorageService
import com.alextos.converter.presentation.scenes.main.MainViewModel
import com.alextos.converter.presentation.scenes.settings.SettingsViewModel
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

fun appModule(delegate: ConverterAppDelegate) = module {
    single { delegate }
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

    single<Settings> { Settings() }
    single<StorageService> { StorageServiceImpl(get()) }
    single { ConverterUseCase()  }
    single { GetFavouriteCurrencyRatesUseCase(get(), delegate) }
    viewModelOf(::ApplicationViewModel)
    viewModelOf(::SettingsViewModel)
}
