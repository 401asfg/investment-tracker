package com.example.investmenttracker.model.database_entries.price_tickers

import com.example.investmenttracker.model.Database
import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.PastPrice
import com.example.investmenttracker.model.database_entries.PriceTicker
import org.json.JSONObject

// TODO: write tests

/**
 * A vehicle that can be invested in
 *
 * @param symbol The symbol of the vehicle
 * @param name The name of the vehicle
 * @param id The id of the row in which to save this entry
 */
class Vehicle(
    val symbol: String,
    val name: String,
    id: Int? = null
) : PriceTicker(Database.VEHICLE_TABLE, id) {
    private val pastPrices: MutableMap<DateTime, PastPrice> = mutableMapOf()

    /**
     * A vehicle that can be invested in
     *
     * @param symbol The symbol of the vehicle
     * @param name The name of the vehicle
     * @param pastPrices The prices of this vehicle on specific dates; if multiple past prices have
     * same date, only the past price with the largest index will be kept
     * @param id The id of the row in which to save this entry
     */
    constructor(
        symbol: String,
        name: String,
        pastPrices: Set<PastPrice>,
        id: Int? = null
    ) : this(symbol, name, id) { addPastPrices(pastPrices) }

    /**
     * Adds the given past prices to this vehicle
     *
     * @param pastPrices The past prices to add; if any of the given past prices has the same date
     * as a previous past price, the given past price overwrites the old past price
     */
    fun addPastPrices(pastPrices: Set<PastPrice>) { pastPrices.forEach { addPastPrice(it) } }

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
     * @return The number of distinct date times recorded in this vehicle
     */
    fun numDates(): Int = pastPrices.size

    /**
     * @return True if this vehicle has no recorded date times; otherwise false
     */
    fun lacksDates(): Boolean = pastPrices.isEmpty()

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("symbol", symbol)
        json.put("name", name)

        return json
    }
}
