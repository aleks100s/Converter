//
//  SpotlightService.swift
//  iosApp
//
//  Created by Alexander on 06.10.2025.
//

import CoreSpotlight

struct SpotlightService {
    func trackLastQuery(
        currencyFrom: String,
        currencyTo: String,
        amountFrom: String,
        amountTo: String
    ) {
        let attributes = createAttributes(
            title: "\(amountFrom) \(currencyFrom) = \(amountTo) \(currencyTo)",
            description: String(localized: "Вы недавно смотрели"),
            url: nil,
            keywords: nil
        )
        index(attributes: attributes)
    }

    private func createAttributes(
        title: String,
        description: String,
        url: URL?,
        keywords: [String]? = nil
    ) -> CSSearchableItemAttributeSet {
        let attributeSet = CSSearchableItemAttributeSet(itemContentType: UTType.text.identifier)
        attributeSet.title = title
        attributeSet.contentDescription = description
        attributeSet.keywords = keywords
        attributeSet.contentURL = url
        return attributeSet
    }
    
    private func index(attributes: CSSearchableItemAttributeSet) {
        let item = CSSearchableItem(uniqueIdentifier: Constants.lastQueryKey, domainIdentifier: Constants.appGroup, attributeSet: attributes)
        item.expirationDate = Date().addingTimeInterval(60 * 60 * 24)
        CSSearchableIndex.default().indexSearchableItems([item]) { error in
            if let error = error {
                print("Indexing error: \(error.localizedDescription)")
            } else {
                print("Search item successfully indexed!")
            }
        }
    }
}
