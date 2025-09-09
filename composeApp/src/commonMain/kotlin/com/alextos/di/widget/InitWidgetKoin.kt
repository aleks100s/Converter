package com.alextos.di.widget

import com.alextos.di.platformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initWidgetKoin(
    config: KoinAppDeclaration? = null
): WidgetDependencyContainer {
    startKoin {
        config?.invoke(this)
        modules(widgetModule, platformModule)
    }
    return WidgetDependencyContainer()
}