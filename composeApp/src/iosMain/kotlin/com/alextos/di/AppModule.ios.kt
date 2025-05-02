package com.alextos.di

import com.alextos.converter.data.database.DatabaseFactory
import com.alextos.converter.data.services.ClipboardServiceImpl
import com.alextos.converter.domain.services.ClipboardService
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { Darwin.create() }
        single { DatabaseFactory() }
        single<ClipboardService> { ClipboardServiceImpl() }
    }