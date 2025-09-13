//
//  FavouriteCurrencyRatesWidget.swift
//  FavouriteCurrencyRatesWidget
//
//  Created by Alexander on 09.09.2025.
//

import WidgetKit
import SwiftUI

struct FavouriteCurrencyRatesWidget: Widget {
    let kind: String = "FavouriteCurrencyRatesWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(
            kind: kind,
            provider: Provider()
        ) { entry in
            FavouriteCurrencyRatesWidgetEntryView(entry: entry)
                .containerBackground(.fill.tertiary, for: .widget)
        }
        .supportedFamilies([.systemMedium])
    }
}

struct Provider: TimelineProvider {
    private let userDefaults = UserDefaults(suiteName: "group.com.alextos.cashback") ?? .standard
    private let decoder = JSONDecoder()

    func placeholder(in context: Context) -> SimpleEntry {
        .default
    }
    
    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> Void) {
        do {
            guard let data = userDefaults.object(forKey: "favourites") as? Data else {
                completion(.default)
                return
            }
            
            let result = try decoder.decode(FavouriteRatesData.self, from: data)
            let entry = SimpleEntry(
                date: result.date,
                rates: result.rates
            )

            completion(entry)
        } catch {
            completion(.default)
        }
    }
    
    func getTimeline(in context: Context, completion: @escaping (Timeline<SimpleEntry>) -> Void) {
        do {
            guard let data = userDefaults.object(forKey: "favourites") as? Data else {
                completion(Timeline(entries: [], policy: .after(.now.addingTimeInterval(60 * 60 * 24))))
                return
            }
            
            let result = try decoder.decode(FavouriteRatesData.self, from: data)
            let entry = SimpleEntry(
                date: result.date,
                rates: result.rates
            )

            completion(Timeline(entries: [entry], policy: .after(.now.addingTimeInterval(60 * 60 * 24))))
        } catch {
            completion(Timeline(entries: [], policy: .after(.now.addingTimeInterval(60 * 60 * 24))))
        }
    }
}

struct SimpleEntry: TimelineEntry {
    static let `default` = SimpleEntry(date: Date(), rates: [.init(code: "USD", rate: 78.45, symbol: "₽")])

    let date: Date
    let rates: [FavouriteRatesData.FavouriteRate]
}

struct FavouriteCurrencyRatesWidgetEntryView : View {
    var entry: Provider.Entry

    var body: some View {
        if let rate = entry.rates.first {
            VStack {
                HStack {
                    Text(rate.code)
                        .font(.title.bold())
                    
                    Text("\(rate.rate) \(rate.symbol)")
                }
                
                Text("\(Text(entry.date, style: .time)) \(Text(entry.date, style: .date))")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
        }
    }
}
