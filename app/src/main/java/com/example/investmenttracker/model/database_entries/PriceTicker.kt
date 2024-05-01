package com.example.investmenttracker.model.database_entries

import com.example.investmenttracker.model.DatabaseEntry
import com.example.investmenttracker.model.DateTime

// TODO: write tests

/**
 * Holds a monetary value, in USD, that changes as time passes
 *
 * @param table The name of the table to save this entry to
 * @param id The id of the row in which to save this entry
 */
abstract class PriceTicker(table: String, id: Int? = null) : DatabaseEntry(table, id) {
    /**
     * @param earlierDateTime The earlier of the two date times
     * @param laterDateTime The later of the two date times
     * @return The difference in price between the later date time and the earlier date time
     * @throws IllegalArgumentException If this does not contain the given earlierDateTime or the
     * given laterDateTime
     */
    fun getPriceDifference(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceAt(laterDateTime) - getPriceAt(earlierDateTime)

    /**
     * @param earlierDateTime The earlier of the two date times
     * @param laterDateTime The later of the two date times
     * @return The rate of return between the later date time and the earlier date time
     * @throws IllegalArgumentException If this does not contain the given earlierDateTime or the
     * given laterDateTime
     */
    fun getRateOfReturn(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceDifference(earlierDateTime, laterDateTime) / getPriceAt(earlierDateTime)

    /**
     * @param dateTime The date and time to get the price at
     * @return The price of this, in USD, at the given dateTime if the given dateTime is contained;
     * otherwise false
     * @throws IllegalArgumentException If this does not contain the given dateTime
     */
    abstract fun getPriceAt(dateTime: DateTime): Float

    /**
     * @param dateTime The date time to check for
     * @return True if this has a record of the given date time; otherwise false
     */
    abstract fun containsDate(dateTime: DateTime): Boolean
}