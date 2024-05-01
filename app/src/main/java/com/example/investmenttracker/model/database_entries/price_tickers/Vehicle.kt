package com.example.investmenttracker.model.database_entries.price_tickers

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.PastPrice
import com.example.investmenttracker.model.database_entries.PriceTicker
import org.json.JSONObject

// TODO: write tests

/**
 * A vehicle that can be invested in
 *
 * @param id The id of the row in which to save this entry
 */
class Vehicle(id: Int? = null) : PriceTicker(id) {
    private val pastPrices: MutableMap<DateTime, PastPrice> = mutableMapOf()

    /**
     * A vehicle that can be invested in
     *
     * @param pastPrices The prices of this vehicle on specific dates; if multiple past prices have
     * same date, only the past price with the largest index will be kept
     * @param id The id of the row in which to save this entry
     */
    constructor(pastPrices: Set<PastPrice>, id: Int? = null) : this(id) {
        pastPrices.forEach { addPastPrice(it) }
    }

    /**
     * Adds the given past price to this vehicle
     *
     * @param pastPrice The past price to add; if the given past price has the same date as a
     * previous past price, the given past price overwrites the old past price
     */
    fun addPastPrice(pastPrice: PastPrice) { pastPrices[pastPrice.dateTime] = pastPrice }

    override fun getPriceAt(dateTime: DateTime): Float {
        if (!containsDate(dateTime))
            throw IllegalArgumentException("Date time not found in vehicle")

        return pastPrices[dateTime]!!.price
    }

    override fun containsDate(dateTime: DateTime): Boolean = pastPrices.containsKey(dateTime)

    /**
     * @return The number of distinct date times recorded in this
     */
    fun numDates(): Int = pastPrices.size

    /**
     * @return True if this has no recorded date times; otherwise false
     */
    fun lacksDates(): Boolean = pastPrices.isEmpty()

    override fun toJson(): JSONObject = JSONObject()
}
