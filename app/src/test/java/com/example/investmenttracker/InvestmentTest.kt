package com.example.investmenttracker

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.Month
import com.example.investmenttracker.model.PastPrice
import com.example.investmenttracker.model.loadables.price_tickers.Investment
import com.example.investmenttracker.model.loadables.price_tickers.Vehicle
import junit.framework.TestCase.assertTrue
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.Assert.fail

// TODO: implement tests

class InvestmentTest {
    lateinit var dateTimeA: DateTime
    lateinit var dateTimeB: DateTime
    lateinit var dateTimeC: DateTime
    lateinit var dateTimeD: DateTime
    lateinit var dateTimeE: DateTime
    lateinit var dateTimeF: DateTime

    val priceA = 0.1f
    val priceB = 100.3f
    val priceC = 4.3f
    val priceD = 1f
    val priceE = 8.8f
    val priceF = 0.0f

    lateinit var pastPriceA: PastPrice
    lateinit var pastPriceB: PastPrice
    lateinit var pastPriceC: PastPrice
    lateinit var pastPriceD: PastPrice
    lateinit var pastPriceE: PastPrice
    lateinit var pastPriceF: PastPrice

    val symbol = "VOO"
    val name = "Vanguard"

    lateinit var vehicle: Vehicle

    val principal = 2.5f

    lateinit var investment: Investment

    @Before
    fun setUp() {
        dateTimeA = DateTime(2000, Month.JANUARY)
        dateTimeB = DateTime(2000, Month.DECEMBER)
        dateTimeC = DateTime(2024, Month.JUNE)
        dateTimeD = DateTime(2030, Month.APRIL)
        dateTimeE = DateTime(2045, Month.JANUARY)
        dateTimeF = DateTime(2050, Month.MARCH)

        pastPriceA = PastPrice(dateTimeA, priceA)
        pastPriceB = PastPrice(dateTimeB, priceB)
        pastPriceC = PastPrice(dateTimeC, priceC)
        pastPriceD = PastPrice(dateTimeD, priceD)
        pastPriceE = PastPrice(dateTimeE, priceE)
        pastPriceF = PastPrice(dateTimeF, priceF)

        vehicle = Vehicle(
            symbol,
            name,
            setOf(pastPriceA, pastPriceB, pastPriceC, pastPriceD, pastPriceE, pastPriceF)
        )

        try {
            investment = Investment(dateTimeC, principal, vehicle)
        } catch (_: IllegalArgumentException) {
            fail()
        }
    }

    @Test
    fun testInit() {
        assertEquals(dateTimeC, investment.dateTime)
        assertEquals(principal, investment.principal)
        assertEquals(vehicle, investment.vehicle)
        assertNull(investment.id)
    }

    @Test
    fun testInitFailsWithoutDateTime() {
        try {
            Investment(DateTime(1900, Month.JUNE), principal, vehicle)
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            Investment(DateTime(2024, Month.JULY), principal, vehicle)
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            Investment(DateTime(2024, Month.JUNE, 0, 0, 0), principal, vehicle)
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            Investment(DateTime(2100, Month.JUNE), principal, vehicle)
            fail()
        } catch (_: IllegalArgumentException) {}

        vehicle.addPastPrice(PastPrice(
            DateTime(2000, Month.JANUARY, 1, 1, 0),
            priceA
        ))

        try {
            Investment(DateTime(2000, Month.JANUARY, 1, 1), principal, vehicle)
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            Investment(DateTime(2000, Month.JANUARY, 1, 1, 1), principal, vehicle)
            fail()
        } catch (_: IllegalArgumentException) {}
    }

    @Test
    fun testInitFailsWithInvalidPrincipal() {
        try {
            Investment(dateTimeC, 0f, vehicle)
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            Investment(dateTimeC, -0.1f, vehicle)
            fail()
        } catch (_: IllegalArgumentException) {}
    }

    @Test
    fun testInitNonNullId() {
        try {
            investment = Investment(dateTimeC, principal, vehicle, 5)
        } catch (_: IllegalArgumentException) {
            fail()
        }

        assertEquals(dateTimeC, investment.dateTime)
        assertEquals(principal, investment.principal)
        assertEquals(vehicle, investment.vehicle)
        assertEquals(5, investment.id)
    }

    @Test
    fun testGetPriceAtDoesntContainDate() {
        try {
            investment.getPriceAt(DateTime(1900, Month.JUNE))
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            investment.getPriceAt(DateTime(2024, Month.JULY))
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            investment.getPriceAt(DateTime(2024, Month.JUNE, 0, 0, 0))
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            investment.getPriceAt(DateTime(2100, Month.JUNE))
            fail()
        } catch (_: IllegalArgumentException) {}

        vehicle.addPastPrice(PastPrice(
            DateTime(2000, Month.JANUARY, 1, 1, 0),
            priceA
        ))

        try {
            investment.getPriceAt(DateTime(2000, Month.JANUARY, 1, 1))
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            investment.getPriceAt(DateTime(2000, Month.JANUARY, 1, 1, 1))
            fail()
        } catch (_: IllegalArgumentException) {}
    }

    @Test
    fun testGetPriceAtContainsDate() {
        assertEquals(priceA, investment.getPriceAt(dateTimeA))
        assertEquals(priceC, investment.getPriceAt(dateTimeC))
        assertEquals(priceF, investment.getPriceAt(dateTimeF))
    }

    @Test
    fun testContainsDateAtDate() {
        assertTrue(investment.containsDate(dateTimeA))
        assertTrue(investment.containsDate(dateTimeC))
        assertTrue(investment.containsDate(dateTimeF))
    }

    @Test
    fun testContainsDateNotAtDate() {
        try {
            investment.containsDate(DateTime(1900, Month.JUNE))
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            investment.containsDate(DateTime(2024, Month.JULY))
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            investment.containsDate(DateTime(2024, Month.JUNE, 0, 0, 0))
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            investment.containsDate(DateTime(2100, Month.JUNE))
            fail()
        } catch (_: IllegalArgumentException) {}

        vehicle.addPastPrice(PastPrice(
            DateTime(2000, Month.JANUARY, 1, 1, 0),
            priceA
        ))

        try {
            investment.containsDate(DateTime(2000, Month.JANUARY, 1, 1))
            fail()
        } catch (_: IllegalArgumentException) {}

        try {
            investment.containsDate(DateTime(2000, Month.JANUARY, 1, 1, 1))
            fail()
        } catch (_: IllegalArgumentException) {}
    }

    @Test
    fun testToJson() {
        val json = JSONObject()
        json.put("date_time", dateTimeC.toJson())
        json.put("principal", principal)
        json.put("vehicle_id", vehicle.id)

        assertEquals(json.toString(), investment.toJson().toString())
    }
}