package com.example.investmenttracker

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.Month
import com.example.investmenttracker.model.toJson
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

class DateTimeTest {
    @Test
    fun testToJsonNoNull() {
        val dateTime = DateTime(2024, Month.JANUARY, 8, 9, 7)
        assertEquals(
            JSONObject(
                """
                {
                    "year": 2024,
                    "month": JANUARY,
                    "date": 8,
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
                    "date": 8,
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
                    "date": 8
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