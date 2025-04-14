package com.alextos.di

import com.alextos.data.ktor.KtorServiceImpl
import com.alextos.domain.services.KtorService
import com.alextos.presentation.scenes.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    factory<KtorService> { KtorServiceImpl() }
    viewModelOf(::MainViewModel)
}