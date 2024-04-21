package com.example.investmenttracker.model

/**
 * A vehicle that can be invested in
 *
 * @param id The unique identifier of this vehicle
 * @param currencyId The unique identifier of the currency of this vehicle is in
 */
class Vehicle(val id: Int, val currencyId: Int) : MonetarilyVariable {
    private val pastPrices: MutableMap<DateTime, Float> = mutableMapOf()

    /**
     * A vehicle that can be invested in
     *
     * @param id The unique identifier of this vehicle
     * @param currencyId The unique identifier of the currency of this vehicle is in
     * @param pastPrices The prices of this vehicle on specific dates; if multiple past prices have
     * same date, only the past price with the largest index will be kept
     */
    constructor(id: Int, currencyId: Int, pastPrices: Set<PastPrice>) : this(id, currencyId) {
        pastPrices.forEach { addPastPrice(it) }
    }

    /**
     * Adds the given past price to this vehicle
     *
     * @param pastPrice The past price to add; if the given past price has the same date as a
     * previous past price, the given past price overwrites the old past price
     */
    fun addPastPrice(pastPrice: PastPrice) { pastPrices[pastPrice.dateTime] = pastPrice.price }

    override fun getPriceAt(dateTime: DateTime): Float {
        if (!containsDate(dateTime))
            throw IllegalArgumentException("Date time not found in vehicle")

        return pastPrices[dateTime]!!
    }

    override fun getPriceDifference(earlierDateTime: DateTime, laterDateTime: DateTime): Float {
        val earlierPrice = pastPrices[earlierDateTime]
            ?: throw IllegalArgumentException("Date time not found in vehicle")

        val laterPrice = pastPrices[laterDateTime]
            ?: throw IllegalArgumentException("Date time not found in vehicle")

        return laterPrice - earlierPrice
    }

    /**
     * @param earlierDateTime The earlier of the two date times
     * @param laterDateTime The later of the two date times
     * @return The rate of return between the later date time and the earlier date time
     * @throws IllegalArgumentException If this does not contain the given earlierDateTime or the
     * given laterDateTime
     */
    fun getRateOfReturn(earlierDateTime: DateTime, laterDateTime: DateTime): Float
            = getPriceDifference(earlierDateTime, laterDateTime) / pastPrices[earlierDateTime]!!

    /**
     * @param dateTime The date time to check for
     * @return True if this has a record of the given date time; otherwise false
     */
    fun containsDate(dateTime: DateTime): Boolean = pastPrices.containsKey(dateTime)

    /**
     * @return The number of date times recorded in this
     */
    fun numDates(): Int = pastPrices.size

    /**
     * @return True if this has no recorded date times; otherwise false
     */
    fun lacksDates(): Boolean = pastPrices.isEmpty()
}
