package com.example.investmenttracker.model.loadables.price_tickers

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.Writable
import com.example.investmenttracker.model.loadables.PriceTicker
import org.json.JSONObject

/**
 * A single investment into a vehicle
 *
 * @param dateTime When this investment was made
 * @param principal The amount of money invested
 * @param vehicle The vehicle that was invested into
 * @param id The id of the row in which to save this entry
 * @throws IllegalArgumentException If the given vehicle does not contain the given dateTime; If
 * the given principal is not greater than zero
 */
class Investment(
    val dateTime: DateTime,
    val principal: Float,
    val vehicle: Vehicle,
    override var id: Int? = null
) : Writable, PriceTicker() {
    init {
        if (!containsDate(dateTime)) throw IllegalArgumentException(
            "Vehicle does not contain a record of its price at the time this investment was made"
        )

        if (principal <= 0f)
            throw IllegalArgumentException("Investment principal is not greater than zero")
    }

    /**
     * @param dateTime The date and time to get the price at
     * @return The price of this, in USD, at the given dateTime if the given dateTime is contained;
     * 0 USD if the given dateTime is earlier than this investment's dateTime
     * @throws IllegalArgumentException If the given dateTime is at the same time as or later than
     * the given dateTime, and this does not contain the given dateTime
     */
    override fun getPriceAt(dateTime: DateTime): Float {
        if (dateTime.isEarlierThan(this.dateTime)) return 0f
        val vehicleRateOfReturn = vehicle.getRateOfReturn(this.dateTime, dateTime)
        return vehicleRateOfReturn * principal + principal
    }

    override fun containsDate(dateTime: DateTime): Boolean = vehicle.containsDate(dateTime)

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("date_time", dateTime.toJson())
        json.put("principal", principal)
        json.put("vehicle_id", vehicle.id)

        return json
    }
}
