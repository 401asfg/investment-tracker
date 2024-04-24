package com.example.investmenttracker.model

import org.json.JSONObject

/**
 * The price, in USD, of an investment vehicle at a specific date and time
 *
 * @param dateTime The date and time at which an investment had the given rate
 * @param price The price, in USD, of an investment at the given date
 */
data class PastPrice(val dateTime: DateTime, val price: Float)

/**
 * @return A json representation of this past price
 */
fun PastPrice.toJson(): JSONObject {
    val json = JSONObject()
    json.put("date_time", dateTime.toJson())
    json.put("price", price)

    return json
}
