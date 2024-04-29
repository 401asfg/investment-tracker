package com.example.investmenttracker.model.database_entries.price_tickers

import com.example.investmenttracker.model.Database
import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.database_entries.PriceTicker
import org.json.JSONArray
import org.json.JSONObject

// TODO: write tests

/**
 * A portfolio of investments
 * 
 * @param usdToBaseCurrencyRate The exchange rate of USD to this portfolio's base currency
 * @param investments The investments made in this portfolio
 * @param id The id of the row in which to save this entry
 */
class Portfolio(
    private val usdToBaseCurrencyRate: Vehicle,
    private val investments: Set<Investment>,
    id: Int? = null
) : PriceTicker(Database.PORTFOLIO_TABLE, id) {
    override fun getPriceAt(dateTime: DateTime): Float {
        val usdToBaseCurrencyRateAtDateTime = usdToBaseCurrencyRate.getPriceAt(dateTime)

        return investments.fold(0f) { baseCurrencySum, investment ->
            if (investment.containsDate(dateTime)) {
                val investmentUsdPrice = investment.getPriceAt(dateTime)
                baseCurrencySum + investmentUsdPrice * usdToBaseCurrencyRateAtDateTime
            } else baseCurrencySum
        }
    }

    override fun containsDate(dateTime: DateTime): Boolean
        = investments.any { it.containsDate(dateTime) }

    override fun toJsonOfClassProperties(): JSONObject {
        val json = JSONObject()
        json.put("usd_to_base_currency_rate", usdToBaseCurrencyRate.toJson())

        val investmentsJsonArray = JSONArray()
        investments.forEach { investmentsJsonArray.put(it.toJson()) }

        json.put("investments", investmentsJsonArray)

        return json
    }
}
