//
//  FavouriteCurrencyRatesWidgetEntryView.swift
//  iosApp
//
//  Created by Alexander on 21.09.2025.
//

import ComposeApp
import SwiftUI
import WidgetKit

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
