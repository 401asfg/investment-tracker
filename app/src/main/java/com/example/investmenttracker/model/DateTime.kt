package com.example.investmenttracker.model

/**
 * A month of a year
 */
enum class Month {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER
}

// FIXME: override hashcode to get latest datetime for null properties?

/**
 * A UTC(+00:00) date and time
 *
 * @param year The year part of this date
 * @param month The month in the year of this date
 * @param date The date in the month this date
 * @param hour The hour in this day
 * @param minute The minute in this hour
 */
data class DateTime(
    val year: Int,
    val month: Month,
    val date: Int? = null,
    val hour: Int? = null,
    val minute: Int? = null
)