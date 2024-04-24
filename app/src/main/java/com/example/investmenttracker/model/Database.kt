package com.example.investmenttracker.model

import com.example.investmenttracker.model.database_entries.price_tickers.Portfolio

/**
 * A database which can have entries saved to it and loaded from it
 */
class Database {
    // TODO: setup HTTP client

    /**
     * Saves the given entry into the database
     *
     * @param entry The entry to save to the database
     * @throws IllegalArgumentException If the database could not save all or part of the given
     * portfolio
     */
    fun save(entry: DatabaseEntry) {
        val json = entry.toJson()
        // TODO: implement
    }

    /**
     * Loads a portfolio with the given id from the database
     *
     * @param id The id to identify the portfolio with in the database
     * @throws IllegalArgumentException If the given id does not correspond to any portfolio in the
     * database
     */
    fun loadPortfolio(id: Int): Portfolio {
        // TODO: implement
    }

// TODO: implement other load functions
}
