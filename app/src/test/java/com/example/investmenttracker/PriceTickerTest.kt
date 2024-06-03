package com.example.investmenttracker

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.Month
import com.example.investmenttracker.model.loadables.PriceTicker
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class PriceTickerMock(override var id: Int? = null) : PriceTicker() {
    private val prices = mapOf(
        DateTime(2024, Month.JANUARY, 8, 9, 7) to 20.11f,
        DateTime(2024, Month.JANUARY, 8, 9, 8) to 4444.5f,
        DateTime(2024, Month.MARCH, 8, 7, 2) to 1.0f,
        DateTime(2024, Month.AUGUST, 12, 19) to 24.2424f,
        DateTime(2020, Month.OCTOBER, 21, 99, 7) to 0.778f
    )

    override fun getPriceAt(dateTime: DateTime): Float {
        if (!containsDate(dateTime)) throw IllegalArgumentException()
        return prices[dateTime]!!
    }

    override fun containsDate(dateTime: DateTime): Boolean = prices.containsKey(dateTime)
}

class PriceTickerTest {
    @Test
    fun testGetPriceDifferenceMissingBothDates() {
        val from = DateTime(1999, Month.JANUARY)
        val to = DateTime(1999, Month.FEBRUARY)
        assertGetPriceDifferenceFails(from, to)
    }

    @Test
    fun testGetPriceDifferenceFromBeforeAllDatesToAfterAllDates() {
        val from = DateTime(1999, Month.JANUARY)
        val to = DateTime(2025, Month.FEBRUARY)
        assertGetPriceDifferenceFails(from, to)
    }

    @Test
    fun testGetPriceDifferenceFromAndToInTheMiddleOfAllDates() {
        val from = DateTime(2024, Month.APRIL, 3)
        val to = DateTime(2024, Month.APRIL, 4)
        assertGetPriceDifferenceFails(from, to)
    }

    @Test
    fun testGetPriceDifferenceMissingFromDate() {
        val from = DateTime(2024, Month.JANUARY, 7, 9, 8)
        val to = DateTime(2024, Month.JANUARY, 8, 9, 8)
        assertGetPriceDifferenceFails(from, to)
    }

    @Test
    fun testGetPriceDifferenceMissingToDate() {
        val from = DateTime(2024, Month.JANUARY, 8, 9, 8)
        val to = DateTime(2024, Month.JANUARY, 9, 9, 8)
        assertGetPriceDifferenceFails(from, to)
    }

    @Test
    fun testGetPriceDifferenceEarlierToDate() {
        val from = DateTime(2024, Month.JANUARY, 8, 9, 8)
        val to = DateTime(2024, Month.JANUARY, 8, 9, 7)
        assertGetPriceDifference(from, to, -4424.39f)
    }

    @Test
    fun testGetPriceDifferenceSameDate() {
        val dateTime = DateTime(2024, Month.JANUARY, 8, 9, 8)
        assertGetPriceDifference(dateTime, dateTime, 0f)
    }

    @Test
    fun testGetPriceDifferenceLaterToDate() {
        val from = DateTime(2020, Month.OCTOBER, 21, 99, 7)
        val to = DateTime(2024, Month.AUGUST, 12, 19)
        assertGetPriceDifference(from, to, 23.4644f)
    }

    @Test
    fun testGetPriceDifferenceFromDateEarlierThanValidByAMinute() {
        val from = DateTime(2020, Month.OCTOBER, 21, 99, 6)
        val to = DateTime(2024, Month.AUGUST, 12, 19)
        assertGetPriceDifferenceFails(from, to)
    }

    @Test
    fun testGetPriceDifferenceFromDateLaterThanValidByAMinute() {
        val from = DateTime(2020, Month.OCTOBER, 21, 99, 9)
        val to = DateTime(2024, Month.AUGUST, 12, 19)
        assertGetPriceDifferenceFails(from, to)
    }

    private fun assertGetPriceDifferenceFails(from: DateTime, to: DateTime) {
        val priceTicker = PriceTickerMock()

        try {
            priceTicker.getPriceDifference(from, to)
            fail()
        } catch (_: IllegalArgumentException) {
        } catch (_: Exception) {
            fail()
        }
    }

    private fun assertGetPriceDifference(
        from: DateTime,
        to: DateTime,
        expectedPriceDifference: Float
    ) {
        val priceTicker = PriceTickerMock()

        try {
            val priceDifference = priceTicker.getPriceDifference(from, to)
            assertEquals(expectedPriceDifference, priceDifference)
        } catch (_: Exception) {
            fail()
        }
    }
}