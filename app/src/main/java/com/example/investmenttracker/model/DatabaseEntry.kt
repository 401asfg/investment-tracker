package com.example.investmenttracker.model

import org.json.JSONObject

/**
 * An entry in a database table
 *
 * @param table The name of the table to save this entry to
 * @param id The id of the row in which to save this entry
 */
abstract class DatabaseEntry(private val table: String, val id: Int?) {
    /**
     * @return A json object containing this class' relevant properties
     */
    protected abstract fun toJsonOfClassProperties(): JSONObject

    /**
     * @return A json object containing this class' relevant properties and the database entry
     * relevant properties
     */
    fun toJson(): JSONObject {
        val json = toJsonOfClassProperties()
        json.put("id", id)
        json.put("table", table)

        return json
    }
}