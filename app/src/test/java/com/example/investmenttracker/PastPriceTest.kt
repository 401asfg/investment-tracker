package com.example.investmenttracker

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.Month
import com.example.investmenttracker.model.PastPrice
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

class PastPriceTest {
    @Test
    fun testToJsonNoNullsInDateTime() {
        val pastPrice = PastPrice(
            DateTime(333, Month.NOVEMBER, 8, 9, 22),
            7.8f
        )

        assertEquals(
            JSONObject(
                """
                {
                    "date_time": {
                        "year": 333,
                        "month": "NOVEMBER",
                        "day": 8,
                        "hour": 9,
                        "minute": 22
                    },
                    "price": 7.8
                }
                """.trimIndent()
            ).toString(),
            pastPrice.toJson().toString()
        )
    }

    @Test
    fun testToJsonNullsInDateTime() {
        val pastPrice = PastPrice(
            DateTime(333, Month.NOVEMBER, 8),
            7.8f
        )

        assertEquals(
            JSONObject(
                """
                {
                    "date_time": {
                        "year": 333,
                        "month": "NOVEMBER",
                        "day": 8
                    },
                    "price": 7.8
                }
                """.trimIndent()
            ).toString(),
            pastPrice.toJson().toString()
        )
    }
}