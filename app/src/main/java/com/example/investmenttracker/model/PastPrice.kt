package com.example.investmenttracker.model

/**
 * The price of an investment vehicle at a specific date and time
 *
 * @param dateTime The date and time at which an investment had the given rate
 * @param price The price of an investment at the given date
 */
data class PastPrice(val dateTime: DateTime, val price: Float)
