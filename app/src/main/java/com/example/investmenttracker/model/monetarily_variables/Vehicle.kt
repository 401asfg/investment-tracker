package com.example.investmenttracker.model.monetarily_variables

import com.example.investmenttracker.data.VEHICLE_TABLE
import com.example.investmenttracker.model.DatabaseEntry
import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.MonetarilyVariable
import com.example.investmenttracker.model.PastPrice
import org.json.JSONObject

/**
 * A vehicle that can be invested in
 *
 * @param id The unique identifier of this vehicle
 * @param currencyId The unique identifier of the currency of this vehicle is in
 */
class Vehicle(
    val currencyId: Int,
    id: Int? = null
) : DatabaseEntry(VEHICLE_TABLE, id), MonetarilyVariable {
    private val pastPrices: MutableMap<DateTime, Float> = mutableMapOf()

    /**
     * A vehicle that can be invested in
     *
     * @param id The unique identifier of this vehicle
     * @param currencyId The unique identifier of the currency of this vehicle is in
     * @param pastPrices The prices of this vehicle on specific dates; if multiple past prices have
     * same date, only the past price with the largest index will be kept
     */
    constructor(
        currencyId: Int,
        pastPrices: Set<PastPrice>,
        id: Int? = null
    ) : this(currencyId, id) {
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

    override fun getRateOfReturn(earlierDateTime: DateTime, laterDateTime: DateTime): Float
            = getPriceDifference(earlierDateTime, laterDateTime) / pastPrices[earlierDateTime]!!

    override fun containsDate(dateTime: DateTime): Boolean = pastPrices.containsKey(dateTime)

    /**
     * @return The number of distinct date times recorded in this
     */
    fun numDates(): Int = pastPrices.size

    /**
     * @return True if this has no recorded date times; otherwise false
     */
    fun lacksDates(): Boolean = pastPrices.isEmpty()

    override fun buildJSON(): JSONObject {
        TODO("Not yet implemented")
    }
}
