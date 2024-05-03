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

        // FIXME: make generic method for building sets

        /**
         * @param json The json that describes the investments to build
         * @return A set of investments, with all of their values obtained from the given json
         */
        private fun buildInvestments(json: JSONArray): Set<Investment> {
            val investments = mutableSetOf<Investment>()

            for (i in 0..json.length()) {
                val investment = buildInvestment(json[i] as JSONObject)
                investments.add(investment)
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
         * @param json The json that describes the vehicles to build
         * @return A set of vehicles, with all of their values obtained from the given json
         */
        private fun buildVehicles(json: JSONArray): Set<Vehicle> {
            val vehicles = mutableSetOf<Vehicle>()

            for (i in 0..json.length()) {
                val vehicle = buildVehicle(json[i] as JSONObject)
                vehicles.add(vehicle)
            }

            return vehicles
        }

        /**
         * @param json The json that describes the vehicle to build
         * @return A vehicle, with all of its values obtained from the given json
         */
        private fun buildVehicle(json: JSONObject): Vehicle {
            val symbol = json.get("symbol") as String
            val name = json.get("name") as String
            val id = json.get("id") as Int

            // FIXME: should this load past prices?

            return Vehicle(symbol, name, id)
        }

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
         * @param json The json that describes the past prices to build
         * @return A set of past prices, with all of their values obtained from the given json
         */
        private fun buildPastPrices(json: JSONArray): Set<PastPrice> {
            val pastPrices = mutableSetOf<PastPrice>()

            for (i in 0..json.length()) {
                val pastPrice = buildPastPrice(json[i] as JSONObject)
                pastPrices.add(pastPrice)
            }

            return pastPrices
        }

        /**
         * @param json The json that describes the past price to build
         * @return A past price, with all of its values obtained from the given json
         */
        private fun buildPastPrice(json: JSONObject): PastPrice {
            val dateTimeJson = json.get("date_time") as JSONObject
            val dateTime = buildDateTime(dateTimeJson)
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
        if (entry.id === null) client.post(entry.table, json)
        else client.put(entry.table, entry.id, json)
    }

    /**
     * Loads a portfolio with the given id from the database
     *
     * @param id The id to identify the portfolio with in the database
     * @throws IOException If the given id does not correspond to any portfolio in the database, or
     * the server could not be reached
     */
    fun loadPortfolio(id: Int): Portfolio {
        val json = client.get(PORTFOLIO_TABLE, id)
        return buildPortfolio(json)
    }

    /**
     * Loads an investment with the given id from the database
     *
     * @param id The id to identify the investment with in the database
     * @throws IOException If the given id does not correspond to any investment in the database,
     * or the server could not be reached
     */
    fun loadInvestment(id: Int): Investment {
        val json = client.get(INVESTMENT_TABLE, id)
        return buildInvestment(json)
    }

    /**
     * Loads a vehicle with the given id from the database
     *
     * @param id The id to identify the vehicle with in the database
     * @throws IOException If the given id does not correspond to any vehicle in the database, or
     * the server could not be reached
     */
    fun loadVehicle(id: Int): Vehicle {
        val json = client.get(VEHICLE_TABLE, id)
        return buildVehicle(json)
    }

    /**
     * Searches for vehicles with symbols or names that contain the given query
     *
     * @param query A series of characters to search for vehicles by
     * @return Vehicles that each have a name or symbol that contains the query within it
     * @throws IOException If the server could not be reached
     */
    fun findVehicles(query: String): Set<Vehicle> {
        val params = mapOf("q" to query)
        val json = client.get(VEHICLE_TABLE, params)
            .get(VEHICLE_TABLE) as JSONArray

        return buildVehicles(json)
    }

    // FIXME: should this be findVehicle?

    /**
     * Searches for all past prices that fall within the bounds of these parameters
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
    fun findPastPrices(
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
            "vehicleId" to vehicleId.toString(),
            "granularity" to granularity.toString(),
            "earliest_date" to earliestDateTime.toJson().toString(),
            "latest_date" to latestDateTime.toJson().toString()
        )

        return client.get(PAST_PRICE_TABLE, params)
            .get(PAST_PRICE_TABLE) as JSONArray
    }
}
