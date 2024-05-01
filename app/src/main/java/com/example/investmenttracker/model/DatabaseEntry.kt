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
     */
    protected abstract fun toJson(): JSONObject

    /**
     * @param omitsTopLevelId True if the produced json should not contain the id of this database
     * entry, otherwise false
     * @return A json object containing this class' relevant properties and its database entry id
     * if omitsTopLevelId is false
     */
    fun toJson(omitsTopLevelId: Boolean = false): JSONObject {
        val json = toJson()
        if (!omitsTopLevelId) json.put("id", id)
        return json
    }
}