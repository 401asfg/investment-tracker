package com.example.investmenttracker.model.loadables.price_tickers

import com.example.investmenttracker.model.DateTime
import com.example.investmenttracker.model.Writable
import com.example.investmenttracker.model.loadables.PriceTicker
import org.json.JSONObject

// TODO: write tests

/**
 * A portfolio of investments
 * 
 * @param usdToBaseCurrencyRate The exchange rate of USD to this portfolio's base currency
 * @param id The id of the row in which to save this entry
 */
class Portfolio(
    private val usdToBaseCurrencyRate: Vehicle,
    override var id: Int? = null
) : Writable, PriceTicker() {
    private val investments: MutableSet<Investment> = mutableSetOf()

    /**
     * A portfolio of investments
     *
     * @param usdToBaseCurrencyRate The exchange rate of USD to this portfolio's base currency
     * @param investments The investments made in this portfolio
     * @param id The id of the row in which to save this entry
     */
    constructor(
        usdToBaseCurrencyRate: Vehicle,
        investments: Set<Investment>,
        id: Int? = null
    ) : this(usdToBaseCurrencyRate, id) { investments.forEach { addInvestment(it) } }

    init { investments.forEach { addInvestment(it) } }

    // FIXME: should this be abstracted into parent interface for vehicle and portfolio?

    /**
     * Adds the given investment to this portfolio
     *
     * @param investment The investment to add
     */
    fun addInvestment(investment: Investment) { investments.add(investment) }

    override fun getPriceAt(dateTime: DateTime): Float {
        val usdToBaseCurrencyRateAtDateTime = usdToBaseCurrencyRate.getPriceAt(dateTime)

        return investments.fold(0f) { baseCurrencySum, investment ->
            if (investment.containsDate(dateTime)) {
                val investmentUsdPrice = investment.getPriceAt(dateTime)
                baseCurrencySum + investmentUsdPrice * usdToBaseCurrencyRateAtDateTime
            } else baseCurrencySum
        }
    }

    override fun containsDate(dateTime: DateTime): Boolean
        = investments.any { it.containsDate(dateTime) }

    /**
     * @return The number of investments in this portfolio
     */
    fun numInvestments(): Int = investments.size

    /**
     * @return True if this portfolio has no investments; otherwise false
     */
    fun lacksInvestments(): Boolean = investments.isEmpty()

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("usd_to_base_currency_rate_vehicle_id", usdToBaseCurrencyRate.id)

        return json
    }
}
