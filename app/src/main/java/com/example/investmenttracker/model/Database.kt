package com.example.investmenttracker.model

import com.example.investmenttracker.model.database_entries.price_tickers.Investment
import com.example.investmenttracker.model.database_entries.price_tickers.Portfolio
import com.example.investmenttracker.model.database_entries.price_tickers.Vehicle
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

// TODO: write tests

/**
 * A database which can have entries saved to it and loaded from it
 *
 * @param client A client that can send messages to the server that holds the actual database
 */
class Database(private val client: Client) {
    companion object {
        const val PAST_PRICE_TABLE = "past_prices"
        const val VEHICLE_TABLE = "vehicles"
        const val INVESTMENT_TABLE = "investments"
        const val PORTFOLIO_TABLE = "portfolios"

        /**
         * @param json The json that describes the portfolio to build
         * @return A portfolio, with all its values obtained from the given json
         */
        private fun buildPortfolio(json: JSONObject): Portfolio {
            val usdToBaseCurrencyRate = buildVehicle(
                json.get("usd_to_base_currency_rate") as JSONObject
            )

            val investments = buildInvestments(json.get("investments") as JSONArray)
            val id = json.get("id") as Int
            return Portfolio(usdToBaseCurrencyRate, investments, id)
        }

        /**
         * @param json The json that describes the investments to build
         * @return A set of investments, with all of their values obtained from the given json
         */
        private fun buildInvestments(json: JSONArray): Set<Investment> {
            val investments = mutableSetOf<Investment>()

            for (i in 0..json.length()) {
                investments.add(buildInvestment(json[i] as JSONObject))
            }

            return investments
        }

        /**
         * @param json The json that describes the investment to build
         * @return An investment, with all of its values obtained from the given json
         */
        private fun buildInvestment(json: JSONObject): Investment {
            val dateTime = buildDateTime(json.get("date_time") as JSONObject)
            val principal = json.get("principal") as Float
            val vehicle = buildVehicle(json.get("vehicle") as JSONObject)
            val id = json.get("id") as Int
            return Investment(dateTime, principal, vehicle, id)
        }

        /**
         * @param json The json that describes the vehicle to build
         * @return A vehicle, with all of its values obtained from the given json
         */
        private fun buildVehicle(json: JSONObject): Vehicle = Vehicle(json.get("id") as Int)

        /**
         * @param json The json that describes the date time to build
         * @return A date time, with all of its values obtained from the given json
         */
        private fun buildDateTime(json: JSONObject): DateTime {
            val year = json.get("year") as Int
            val month = json.get("month") as Month
            var date: Int? = null
            var hour: Int? = null
            var minute: Int? = null

            if (json.has("date")) date = json.get("date") as Int
            if (json.has("hour")) hour = json.get("hour") as Int
            if (json.has("minute")) minute = json.get("minute") as Int

            return DateTime(year, month, date, hour, minute)
        }

        /**
         * @param json The json that describes the past price to build
         * @return A past price, with all of its values obtained from the given json
         */
        private fun buildPastPrice(json: JSONObject): PastPrice {
            val dateTime = json.get("date_time") as DateTime
            val price = json.get("price") as Float
            return PastPrice(dateTime, price)
        }
    }

    /**
     * Saves the given entry into the database
     *
     * @param entry The entry to save to the database
     * @throws IOException If the database could not save all or part of the given portfolio
     */
    fun save(entry: DatabaseEntry) {
        val json = entry.toJson(true)
        if (entry.id === null) client.post(json)
        else client.put(json)
    }

    /**
     * Loads a portfolio with the given id from the database
     *
     * @param id The id to identify the portfolio with in the database
     * @throws IOException If the given id does not correspond to any portfolio in the database
     */
    fun loadPortfolio(id: Int): Portfolio {
        val json = clientGet(PORTFOLIO_TABLE, id)
        return buildPortfolio(json)
    }

    /**
     * Loads an investment with the given id from the database
     *
     * @param id The id to identify the investment with in the database
     * @throws IOException If the given id does not correspond to any investment in the database
     */
    fun loadInvestment(id: Int): Investment {
        val json = clientGet(INVESTMENT_TABLE, id)
        return buildInvestment(json)
    }

    /**
     * Loads a vehicle with the given id from the database
     *
     * @param id The id to identify the vehicle with in the database
     * @throws IOException If the given id does not correspond to any vehicle in the database
     */
    fun loadVehicle(id: Int): Vehicle {
        val json = clientGet(VEHICLE_TABLE, id)
        return buildVehicle(json)
    }

    /**
     * Loads all past prices that fall within the bounds of these parameters
     *
     * @param vehicleId The id of the vehicle all past prices must belong to
     * @param granularity No past price produced can have a date and time that is a fraction of
     * this granularity (i.e. if granularity is a date, all past prices produced must be at the end
     * of the day or have a null date field)
     * @param earliestDateTime The earliest date and time that any past price can be from
     * @param latestDateTime The latest date and time that any past price can be from
     * @return Produces all past prices that have the given vehicleId, come after or at the give
     * earliestDateTime, before or at the given latestDateTime, and are not fractions of the given
     * granularity
     * @throws IOException If the given id does not correspond to any vehicle in the database
     */
    fun loadPastPrices(
        vehicleId: Int,
        granularity: TimeGranularity,
        earliestDateTime: DateTime,
        latestDateTime: DateTime
    ): Set<PastPrice> {
        val json = clientGetPastPrices(
            vehicleId,
            granularity,
            earliestDateTime,
            latestDateTime
        )

        val pastPrices: MutableSet<PastPrice> = mutableSetOf()
        for (i in 0..json.length()) { pastPrices.add(buildPastPrice(json[i] as JSONObject)) }
        return pastPrices
    }

    /**
     * Gets a response from the client, with a request that contains the given table and id
     *
     * @param table The table to send in the request
     * @param id The id to send in the request
     * @return The body of the response to this request
     * @throws IOException If the client fails to send this request
     */
    private fun clientGet(table: String, id: Int): JSONObject {
        val params = mapOf("table" to table, "id" to id.toString())
        return client.get(params)
    }

    /**
     * Gets a response from the client containing all past prices that fall within the bounds of
     * these parameters
     *
     * @param vehicleId The id of the vehicle all past prices must belong to
     * @param granularity No past price produced can have a date and time that is a fraction of
     * this granularity (i.e. if granularity is a date, all past prices produced must be at the end
     * of the day or have a null date field)
     * @param earliestDateTime The earliest date and time that any past price can be from
     * @param latestDateTime The latest date and time that any past price can be from
     * @return Produces all past prices that have the given vehicleId, come after or at the give
     * earliestDateTime, before or at the given latestDateTime, and are not fractions of the given
     * granularity
     * @throws IOException If the client fails to send this request
     */
    private fun clientGetPastPrices(
        vehicleId: Int,
        granularity: TimeGranularity,
        earliestDateTime: DateTime,
        latestDateTime: DateTime
    ): JSONArray {
        val params = mapOf(
            "table" to PAST_PRICE_TABLE,
            "id" to vehicleId.toString(),
            "granularity" to granularity.toString(),
            "earliest_date" to earliestDateTime.toJson().toString(),
            "latest_date" to latestDateTime.toJson().toString()
        )

        return client.get(params).get("past_prices") as JSONArray
    }
}
