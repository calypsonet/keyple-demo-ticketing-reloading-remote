package org.calypsonet.keyple.demo.reload.remote

class CardRepository {
    private var cardContracts: CardContracts? = null

    fun getCardContracts(): CardContracts {
        return cardContracts ?: CardContracts()
    }

    fun saveCardContracts(contracts: CardContracts) {
        cardContracts = contracts
    }

    fun clear() {
        cardContracts = null
    }
}