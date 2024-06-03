package com.example.investmenttracker.model.loadables

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.Loadable

/**
 * Holds a monetary value, in USD, that changes as time passes
 */
abstract class PriceTicker : Loadable {
    /**
     * @param from The earlier of the two date times
     * @param to The later of the two date times
     * @return The difference in price between the later date time and the earlier date time
     * @throws IllegalArgumentException If this does not contain the given earlierDateTime or the
     * given laterDateTime
     */
    fun getPriceDifference(from: DateTime, to: DateTime): Float
        = getPriceAt(to) - getPriceAt(from)

    /**
     * @param from The earlier of the two date times
     * @param to The later of the two date times
     * @return The rate of return between the later date time and the earlier date time
     * @throws IllegalArgumentException If this does not contain the given earlierDateTime or the
     * given laterDateTime
     */
    fun getRateOfReturn(from: DateTime, to: DateTime): Float
        = getPriceDifference(from, to) / getPriceAt(from)

    /**
     * @param dateTime The date and time to get the price at
     * @return The price of this, in USD, at the given dateTime if the given dateTime is contained
     * @throws IllegalArgumentException If this does not contain the given dateTime
     */
    abstract fun getPriceAt(dateTime: DateTime): Float

    /**
     * @param dateTime The date time to check for
     * @return True if this has a record of the given date time; otherwise false
     */
    abstract fun containsDate(dateTime: DateTime): Boolean
}