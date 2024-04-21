package com.example.investmenttracker.model

import org.json.JSONObject

/**
 * An entry in a database table
 *
 * @param table The name of the table to save this entry to
 * @param id The id of the row in which to save this entry
 */
abstract class DatabaseEntry(val table: String, val id: Int?) {
    /**
     * @return A json object containing this class' relevant properties
     * @throws IllegalArgumentException If the table does not contain a row corresponding to this
     * entry's id
     */
    protected abstract fun buildJSON(): JSONObject

    /**
     * Saves this entry into this entry's table, at the row with this entry's id; If the id is
     * null, creates a new row in the table; If the id is not null, updates the existing row in the
     * table with this entry's information
     */
    fun save() {
        val json = buildJSON()
        // TODO: implement http post request sender
    }
}