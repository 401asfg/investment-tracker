package com.example.investmenttracker.model


import org.json.JSONObject

/**
 * A smallest granularity to which a given period of time is measured
 */
enum class TimeGranularity {
    YEAR,
    MONTH,
    DATE,
    HOUR,
    MINUTE
}

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
 * @param day The date in the month this date
 * @param hour The hour in this day
 * @param minute The minute in this hour
 */
data class DateTime(
    val year: Int,
    val month: Month,
    val day: Int? = null,
    val hour: Int? = null,
    val minute: Int? = null
) : Writable {
    // TODO: test isEarlierThan

    /**
     * @param that The date time to compare this to
     * @return True if this is earlier than that; otherwise, false; if either date time has a null
     * field, that field is not compared
     */
    fun isEarlierThan(that: DateTime): Boolean {
        // FIXME: refactor to use the date time's distance in minutes from 0-1-1 00:00 for comparison
        if (this.year < that.year) return true
        if (this.year > that.year) return false

        if (toNum(this.month) < toNum(that.month)) return true
        if (toNum(this.month) > toNum(that.month)) return false

        if (this.day === null || that.day === null) return false
        if (this.day < that.day) return true
        if (this.day > that.day) return false

        if (this.hour === null || that.hour === null) return false
        if (this.hour < that.hour) return true
        if (this.hour > that.hour) return false

        if (this.minute === null || that.minute === null) return false
        if (this.minute < that.minute) return true

        return false
    }

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("year", year)
        json.put("month", month)
        json.put("day", day)
        json.put("hour", hour)
        json.put("minute", minute)

        return json
    }

    /**
     * @param month The month to convert into its corresponding number
     * @return The number representation of the month
     */
    private fun toNum(month: Month): Int {
        return when (month) {
            Month.JANUARY -> 1
            Month.FEBRUARY -> 2
            Month.MARCH -> 3
            Month.APRIL -> 4
            Month.MAY -> 5
            Month.JUNE -> 6
            Month.JULY -> 7
            Month.AUGUST -> 8
            Month.SEPTEMBER -> 9
            Month.OCTOBER -> 10
            Month.NOVEMBER -> 11
            Month.DECEMBER -> 12
        }
    }
}
