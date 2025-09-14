package com.alextos.di

import com.alextos.di.ConverterAppDelegate
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    delegate: ConverterAppDelegate,
    config: KoinAppDeclaration? = null
) {
    startKoin {
        config?.invoke(this)
        modules(appModule(delegate), platformModule)
    }
}