package org.calypsonet.keyple.demo.reload.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class InputDataIncreaseCounter(
    val counterIncrement: String = "1"
)

@Serializable
class InputData

@Serializable
data class OutputData (
    val items: List<String>?,
    val statusCode: Int,
    val message: String,
)

class CardContractsBuilder {
    fun build(content: String) : CardContracts {
        val contracts: MutableList<Contract> = mutableListOf()
        val items : Array<String> = Json.decodeFromString(content)
        items.forEach {
            val contract = Contract().decodeContract(it)
            contracts.add(contract)
        }
        return CardContracts(contracts)
    }
}

@Serializable
data class CardContracts(
    val contracts: List<Contract> = emptyList()
)

@Serializable
class Contract(
    var versionNumber: String = "",
    var tarif: String = "",
    var saleDate: String = "",
    var validityEndDate: String = "",
    var saleSam: Int = 0,
    var saleCounter: Int = 0,
    var authKvc: Int = 0,
    var authenticator: Int = 0,
    var counterValue: Int = 0
) {
    fun decodeContract(str: String) : Contract {
        str.lines().forEach {
            if (it.trim().isNotEmpty()) {
                val splitted = it.split(": ")
                if (splitted.size != 2) {
                    throw IllegalArgumentException("Data is not properly formated: $it")
                }
                val key = splitted[0]
                val value = splitted[1]
                when (key) {
                    "Contract Version Number" -> versionNumber = value
                    "Contract Tariff" -> tarif = value
                    "Contract Sale Date" -> saleDate = value
                    "Contract Sale Sam" -> saleSam = value.trim().toInt()
                    "Contract Auth Kvc" -> authKvc = value.trim().toInt()
                    "Contract Authenticator" -> authenticator = value.trim().toInt()
                    "Counter Value" -> counterValue = value.trim().toInt()
                    "Contract Validity End Date" -> validityEndDate = value
                    "Contract Sale Counter" -> saleCounter = value.trim().toInt()
                    else -> throw IllegalArgumentException("Invalid key: $key")
                }
            }
        }
        return this
    }
}
