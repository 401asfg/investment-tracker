package com.example.investmenttracker.model.monetarily_variables

import com.example.investmenttracker.data.PORTFOLIO_TABLE
import com.example.investmenttracker.model.DatabaseEntry
import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.MonetarilyVariable
import org.json.JSONObject

/**
 * A portfolio of investments
 * 
 * @param usdToBaseCurrencyRate The exchange rate of USD to this portfolio's base currency
 * @param investments The investments made in this portfolio
 * @param id The id of the row in which to save this entry
 * @param createdAt The date and time that this entry was created at, in the database
 * @param updatedAt The date and time that this entry was last update at, in the database
 */
class Portfolio(
    val usdToBaseCurrencyRate: Vehicle,
    val investments: Set<Investment>,
    id: Int? = null,
    createdAt: DateTime? = null,
    updatedAt: DateTime? = null
) : DatabaseEntry(PORTFOLIO_TABLE, id, createdAt, updatedAt), MonetarilyVariable {
    override fun getPriceAt(dateTime: DateTime): Float {
        val usdToBaseCurrencyRateAtDateTime = usdToBaseCurrencyRate.getPriceAt(dateTime)

        return investments.fold(0f) { baseCurrencySum, investment ->
            if (investment.containsDate(dateTime)) {
                val investmentUsdPrice = investment.getPriceAt(dateTime)
                baseCurrencySum + investmentUsdPrice * usdToBaseCurrencyRateAtDateTime
            } else baseCurrencySum
        }
    }

    override fun getPriceDifference(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceAt(laterDateTime) - getPriceAt(earlierDateTime)

    override fun getRateOfReturn(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceDifference(earlierDateTime, laterDateTime) / getPriceAt(earlierDateTime)

    override fun containsDate(dateTime: DateTime): Boolean
        = investments.any { it.containsDate(dateTime) }

    override fun toJson(): JSONObject {
        TODO("Not yet implemented")
    }
}
