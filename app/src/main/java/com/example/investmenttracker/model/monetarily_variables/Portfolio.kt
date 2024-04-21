package com.example.investmenttracker.model.monetarily_variables

import com.example.investmenttracker.data.PORTFOLIO_TABLE
import com.example.investmenttracker.model.DatabaseEntry
import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.MonetarilyVariable
import org.json.JSONObject

/**
 * A portfolio of investments
 * 
 * @param currencyId The unique identifier of the currency of this vehicle is in
 * @param investments The investments made in this portfolio
 */
class Portfolio(
    val currencyId: Int,
    val investments: Set<Investment>,
    id: Int? = null
) : DatabaseEntry(PORTFOLIO_TABLE, id), MonetarilyVariable {
    override fun getPriceAt(dateTime: DateTime): Float
        = investments.fold(0f) { sum, investment ->
            if (investment.containsDate(dateTime)) sum + investment.getPriceAt(dateTime)
            else sum
        }

    override fun getPriceDifference(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceAt(laterDateTime) - getPriceAt(earlierDateTime)

    override fun getRateOfReturn(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceDifference(earlierDateTime, laterDateTime) / getPriceAt(earlierDateTime)

    override fun containsDate(dateTime: DateTime): Boolean
        = investments.any { it.containsDate(dateTime) }

    override fun buildJSON(): JSONObject {
        TODO("Not yet implemented")
    }
}
