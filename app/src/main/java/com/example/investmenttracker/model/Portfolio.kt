package com.example.investmenttracker.model

/**
 * A portfolio of investments
 * 
 * @param currencyId The unique identifier of the currency of this vehicle is in
 * @param investments The investments made in this portfolio
 */
class Portfolio(val currencyId: Int, val investments: Set<Investment>) : MonetarilyVariable {
    fun getPriceAt(dateTime: DateTime): Float
        = investments.sumOf { if (it.containsDate(dateTime)) it.getPriceAt(dateTime) else 0 }

    fun getPriceDifference(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceAt(laterDateTime) - getPriceAt(earlierDateTime)

    fun getRateOfReturn(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceDifference(earlierDateTime, laterDateTime) / getPriceAt(earlierDateTime)

    fun containsDate(dateTime: DateTime): Boolean
        = investments.any { it.containsDate(dateTime) }
}
