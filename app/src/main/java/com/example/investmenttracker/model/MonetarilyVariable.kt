package com.example.investmenttracker.model

/**
 * Holds a monetary value that changes as time passes
 */
interface MonetarilyVariable {
    /**
     * @param dateTime The date and time to get the price at
     * @return The price of this at the given dateTime if the given dateTime is contained;
     * otherwise false
     * @throws IllegalArgumentException If this does not contain the given dateTime
     */
    fun getPriceAt(dateTime: DateTime): Float

    /**
     * @param earlierDateTime The earlier of the two date times
     * @param laterDateTime The later of the two date times
     * @return The difference in price between the later date time and the earlier date time
     * @throws IllegalArgumentException If this does not contain the given earlierDateTime or the
     * given laterDateTime
     */
    fun getPriceDifference(earlierDateTime: DateTime, laterDateTime: DateTime): Float
}