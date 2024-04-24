package com.example.investmenttracker.model.database_entries.price_tickers

import com.example.investmenttracker.data.INVESTMENT_TABLE
import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.database_entries.PriceTicker
import com.example.investmenttracker.model.toJson
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
    id: Int? = null
) : PriceTicker(INVESTMENT_TABLE, id) {
    init {
        if (!vehicle.containsDate(dateTime)) throw IllegalArgumentException(
            "Vehicle does not contain a record of its price at the time this investment was made"
        )

        if (principal <= 0)
            throw IllegalArgumentException("Investment principal is not greater than zero")
    }

    override fun getPriceAt(dateTime: DateTime): Float {
        val vehicleRateOfReturn = vehicle.getRateOfReturn(this.dateTime, dateTime)
        return vehicleRateOfReturn * principal + principal
    }

    override fun containsDate(dateTime: DateTime): Boolean = vehicle.containsDate(dateTime)

    override fun toJsonOfClassProperties(): JSONObject {
        val json = JSONObject()
        json.put("date_time", dateTime.toJson())
        json.put("principal", principal)
        json.put("vehicle", vehicle.toJson())

        return json
    }
}