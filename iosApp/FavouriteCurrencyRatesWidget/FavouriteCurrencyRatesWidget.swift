//
//  FavouriteCurrencyRatesWidget.swift
//  FavouriteCurrencyRatesWidget
//
//  Created by Alexander on 09.09.2025.
//

import ComposeApp
import WidgetKit
import SwiftUI

struct FavouriteCurrencyRatesWidget: Widget {
    let kind: String = "FavouriteCurrencyRatesWidget"

    private let container: WidgetDependencyContainer
    
    init() {
        InitWidgetKoinKt.doInitWidgetKoin()
        container = WidgetDependencyContainer()
    }

    var body: some WidgetConfiguration {
        StaticConfiguration(
            kind: kind,
            provider: Provider(useCase: container.getFavouriteCurrencyRatesUseCase)
        ) { entry in
            FavouriteCurrencyRatesWidgetEntryView(entry: entry)
                .containerBackground(.fill.tertiary, for: .widget)
        }
        .supportedFamilies([.systemSmall, .systemMedium, .systemLarge])
    }
}

struct Provider: TimelineProvider {
    let useCase: GetFavouriteCurrencyRatesUseCase

    private let userDefaults = UserDefaults(suiteName: Constants.appGroup) ?? .standard
    private let decoder = JSONDecoder()

    func placeholder(in context: Context) -> FavouriteCurrencyRatesWidgetEntry {
        .default
    }
    
    func getSnapshot(in context: Context, completion: @escaping (FavouriteCurrencyRatesWidgetEntry) -> Void) {
        guard !context.isPreview else {
            completion(.default)
            return
        }

        guard let data = userDefaults.object(forKey: Constants.widgetDataKey) as? Data else {
            completion(.default)
            return
        }
        
        guard let sharedData = try? decoder.decode(SharedWidgetData.self, from: data) else {
            completion(.default)
            return
        }
        
        Task {
            let result = try await useCase.invoke(favourites: sharedData.favouriteCurrencies, main: sharedData.mainCurrency)
            let entry = FavouriteCurrencyRatesWidgetEntry(date: .now, rates: result ?? [])
            completion(entry)
        }
    }
    
    func getTimeline(in context: Context, completion: @escaping (Timeline<FavouriteCurrencyRatesWidgetEntry>) -> Void) {
        guard let data = userDefaults.object(forKey: Constants.widgetDataKey) as? Data else {
            completion(Timeline(entries: [], policy: .after(.now.addingTimeInterval(60 * 60 * 12))))
            return
        }
        
        guard let sharedData = try? decoder.decode(SharedWidgetData.self, from: data) else {
            completion(Timeline(entries: [], policy: .after(.now.addingTimeInterval(60 * 60 * 12))))
            return
        }
        
        Task {
            let result = try await useCase.invoke(favourites: sharedData.favouriteCurrencies, main: sharedData.mainCurrency)
            let entry = FavouriteCurrencyRatesWidgetEntry(date: .now, rates: result ?? [])
            completion(Timeline(entries: [entry], policy: .after(.now.addingTimeInterval(60 * 60 * 12))))
        }
    }
}

struct FavouriteCurrencyRatesWidgetEntry: TimelineEntry {
    static let `default` = FavouriteCurrencyRatesWidgetEntry(
        date: Date(),
        rates: [
            FavouriteCurrency(currencyCode: CurrencyCode.usd, rate: "84.38 ₽"),
            FavouriteCurrency(currencyCode: CurrencyCode.eur, rate: "99.33 ₽"),
            FavouriteCurrency(currencyCode: CurrencyCode.cny, rate: "11.83 ₽"),
            FavouriteCurrency(currencyCode: CurrencyCode.jpy, rate: "0.573 ₽")
        ]
    )

    let date: Date
    let rates: [FavouriteCurrency]
}
