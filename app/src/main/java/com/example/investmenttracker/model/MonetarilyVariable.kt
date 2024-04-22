package com.example.investmenttracker.model

/**
 * Holds a monetary value, in USD, that changes as time passes
 */
interface MonetarilyVariable {
    /**
     * @param dateTime The date and time to get the price at
     * @return The price of this, in USD, at the given dateTime if the given dateTime is contained;
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

    /**
     * @param earlierDateTime The earlier of the two date times
     * @param laterDateTime The later of the two date times
     * @return The rate of return between the later date time and the earlier date time
     * @throws IllegalArgumentException If this does not contain the given earlierDateTime or the
     * given laterDateTime
     */
    fun getRateOfReturn(earlierDateTime: DateTime, laterDateTime: DateTime): Float

    /**
     * @param dateTime The date time to check for
     * @return True if this has a record of the given date time; otherwise false
     */
    fun containsDate(dateTime: DateTime): Boolean
}