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

struct FavouriteCurrencyRatesWidgetEntryView : View {
    var entry: Provider.Entry

    @Environment(\.widgetFamily) private var widgetFamily

    var body: some View {
        switch widgetFamily {
        case .systemSmall:
            SmallWidgetView(entry: entry)
        case .systemMedium:
            MediumWidgetView(entry: entry)
        case .systemLarge:
            LargeWidgetView(entry: entry)
        case .accessoryCorner:
            SmallWidgetView(entry: entry)
        case .accessoryCircular:
            SmallWidgetView(entry: entry)
        case .accessoryRectangular:
            SmallWidgetView(entry: entry)
        case .accessoryInline:
            SmallWidgetView(entry: entry)
        default:
            EmptyView()
        }
    }
}

private struct SmallWidgetView: View {
    let entry: FavouriteCurrencyRatesWidgetEntry

    var body: some View {
        if let rate = entry.rates.first {
            FavouriteCurrencyView(favoriteCurrency: rate)
        } else {
            Text("Выберите избранные валюты в настройках приложения")
        }
    }
}

private struct MediumWidgetView: View {
    let entry: FavouriteCurrencyRatesWidgetEntry

    var body: some View {
        switch entry.rates.count {
        case 0:
            Text("Выберите избранные валюты в настройках приложения")
        case 1:
            FavouriteCurrencyView(favoriteCurrency: entry.rates[0])
        default:
            HStack {
                Spacer()

                FavouriteCurrencyView(favoriteCurrency: entry.rates[0])
                
                Spacer()
                
                FavouriteCurrencyView(favoriteCurrency: entry.rates[1])
                
                Spacer()
            }
        }
    }
}

private struct LargeWidgetView: View {
    let entry: FavouriteCurrencyRatesWidgetEntry
    
    var body: some View {
        switch entry.rates.count {
        case 0:
            Text("Выберите избранные валюты в настройках приложения")
        case 1:
            FavouriteCurrencyView(favoriteCurrency: entry.rates[0])
        case 2:
            HStack {
                Spacer()

                FavouriteCurrencyView(favoriteCurrency: entry.rates[0])
                
                Spacer()

                FavouriteCurrencyView(favoriteCurrency: entry.rates[1])
                
                Spacer()
            }
        case 3:
            VStack {
                Spacer()

                HStack {
                    Spacer()

                    FavouriteCurrencyView(favoriteCurrency: entry.rates[0])
                    
                    Spacer()

                    FavouriteCurrencyView(favoriteCurrency: entry.rates[1])

                    Spacer()
                }
                
                Spacer()
                
                FavouriteCurrencyView(favoriteCurrency: entry.rates[2])
                
                Spacer()
            }
        default:
            VStack {
                Spacer()

                HStack {
                    Spacer()

                    FavouriteCurrencyView(favoriteCurrency: entry.rates[0])
                    
                    Spacer()

                    FavouriteCurrencyView(favoriteCurrency: entry.rates[1])
                    
                    Spacer()
                }
                
                Spacer()
                
                HStack {
                    Spacer()

                    FavouriteCurrencyView(favoriteCurrency: entry.rates[2])
                    
                    Spacer()

                    FavouriteCurrencyView(favoriteCurrency: entry.rates[3])
                    
                    Spacer()
                }
                
                Spacer()
            }
        }
    }
}

private struct FavouriteCurrencyView: View {
    let favoriteCurrency: FavouriteCurrency
    
    var body: some View {
        VStack {
            Text("\(favoriteCurrency.currencyCode.emoji) \(favoriteCurrency.currencyCode.name)")
                .font(.title.bold())
            
            Text(favoriteCurrency.rate)
                .font(.title)
        }
    }
}
