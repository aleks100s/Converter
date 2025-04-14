package com.alextos.di

import com.alextos.data.ktor.KtorServiceImpl
import com.alextos.domain.services.KtorService
import org.koin.dsl.module

val appModule = module {
    factory<KtorService> { KtorServiceImpl() }
}