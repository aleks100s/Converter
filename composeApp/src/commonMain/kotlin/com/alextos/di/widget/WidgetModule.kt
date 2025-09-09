package com.alextos.di.widget

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.alextos.common.data.HttpClientFactory
import com.alextos.converter.data.CurrencyRepositoryImpl
import com.alextos.converter.data.database.AppDatabase
import com.alextos.converter.data.database.DatabaseFactory
import com.alextos.converter.data.database.DatabaseSeeder
import com.alextos.converter.data.network.KtorRemoteCurrencyDataSource
import com.alextos.converter.data.network.RemoteCurrencyDataSource
import com.alextos.converter.domain.favourites.GetFavouriteCurrencyRatesUseCase
import com.alextos.converter.domain.repository.CurrencyRepository
import io.ktor.client.HttpClient
import org.koin.dsl.module

val widgetModule = module {
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

    single { GetFavouriteCurrencyRatesUseCase(get()) }
}