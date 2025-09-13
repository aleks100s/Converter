//
//  FavouriteRatesData.swift
//  iosApp
//
//  Created by Alexander on 13.09.2025.
//


import ComposeApp

struct FavouriteRatesData: Codable {
    struct FavouriteRate: Codable {
        let code: String
        let rate: Double
        let symbol: String
    }

    let date: Date
    let rates: [FavouriteRate]
    
    init(result: GetFavouriteCurrencyRatesUseCase.Result) {
        self.date = Date(timeIntervalSince1970: Double(result.timestamp))
        self.rates = result.currencyRates.map {
            FavouriteRate(code: $0.code.name, rate: $0.rate, symbol: $0.sign)
        }
    }
}
