package com.example.investmenttracker.model.monetarily_variables

import com.example.investmenttracker.data.INVESTMENT_TABLE
import com.example.investmenttracker.model.DatabaseEntry
import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.MonetarilyVariable
import org.json.JSONObject

/**
 * A single investment into a vehicle
 *
 * @param dateTime When this investment was made
 * @param principal The amount of money invested
 * @param vehicle The vehicle that was invested into
 * @param id The id of the row in which to save this entry
 * @param createdAt The date and time that this entry was created at, in the database
 * @param updatedAt The date and time that this entry was last update at, in the database
 * @throws IllegalArgumentException If the given vehicle does not contain the given dateTime; If
 * the given principal is not greater than zero
 */
class Investment(
    val dateTime: DateTime,
    val principal: Float,
    val vehicle: Vehicle,
    id: Int? = null,
    createdAt: DateTime? = null,
    updatedAt: DateTime? = null
) : DatabaseEntry(INVESTMENT_TABLE, id, createdAt, updatedAt), MonetarilyVariable {

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

    override fun getPriceDifference(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = getPriceAt(laterDateTime) - getPriceAt(earlierDateTime)


    override fun getRateOfReturn(earlierDateTime: DateTime, laterDateTime: DateTime): Float
        = vehicle.getRateOfReturn(earlierDateTime, laterDateTime)

    override fun containsDate(dateTime: DateTime): Boolean = vehicle.containsDate(dateTime)

    override fun toJson(): JSONObject {
        TODO("Not yet implemented")
    }
}
