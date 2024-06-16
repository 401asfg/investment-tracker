package com.example.investmenttracker

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.Month
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

class DateTimeTest {
    @Test
    fun testIsEarlierThanNoNullFields() {
        // TODO: write
    }

    @Test
    fun testIsEarlierThanNullThisFields() {
        // TODO: write
    }

    @Test
    fun testIsEarlierThanNullThatFields() {
        // TODO: write
    }

    @Test
    fun testIsEarlierThanAllNullFields() {
        // TODO: write
    }

    @Test
    fun testToJsonNoNull() {
        val dateTime = DateTime(2024, Month.JANUARY, 8, 9, 7)
        assertEquals(
            JSONObject(
                """
                {
                    "year": 2024,
                    "month": JANUARY,
                    "day": 8,
                    "hour": 9,
                    "minute": 7
                }
                """.trimIndent()
            ).toString(),
            dateTime.toJson().toString()
        )
    }

    @Test
    fun testToJsonMinuteNull() {
        val dateTime = DateTime(9, Month.DECEMBER, 8, 9)
        assertEquals(
            JSONObject(
                """
                {
                    "year": 9,
                    "month": DECEMBER,
                    "day": 8,
                    "hour": 9
                }
                """.trimIndent()
            ).toString(),
            dateTime.toJson().toString()
        )
    }

    @Test
    fun testToJsonMinuteHourNull() {
        val dateTime = DateTime(2024, Month.JANUARY, 8)
        assertEquals(
            JSONObject(
                """
                {
                    "year": 2024,
                    "month": JANUARY,
                    "day": 8
                }
                """.trimIndent()
            ).toString(),
            dateTime.toJson().toString()
        )
    }

    @Test
    fun testToJsonMinuteHourDateNull() {
        val dateTime = DateTime(2024, Month.JANUARY)
        assertEquals(
            JSONObject(
                """
                {
                    "year": 2024,
                    "month": JANUARY
                }
                """.trimIndent()
            ).toString(),
            dateTime.toJson().toString()
        )
    }
}