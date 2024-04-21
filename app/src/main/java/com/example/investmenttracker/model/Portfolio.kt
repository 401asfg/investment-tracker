package com.example.investmenttracker.model

/**
 * A portfolio of investments
 * 
 * @param currencyId The unique identifier of the currency of this vehicle is in
 * @param investments The investments made in this portfolio
 */
class Portfolio(val currencyId: Int, val investments: Set<Investment>) : MonetarilyVariable {
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
}
