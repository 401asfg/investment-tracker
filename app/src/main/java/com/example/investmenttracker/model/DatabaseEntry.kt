package com.example.investmenttracker.model

import org.json.JSONObject

/**
 * An entry in a database table
 *
 * @param table The name of the table to save this entry to
 * @param id The id of the row in which to save this entry
 * @param createdAt The date and time that this entry was created at, in the database
 * @param updatedAt The date and time that this entry was last update at, in the database
 */
abstract class DatabaseEntry(
    val table: String,
    val id: Int?,
    val createdAt: DateTime?,
    val updatedAt: DateTime?
) {
    /**
     * @param foreignKeys The foreign keys of this database entry
     * @return A json object containing this class' relevant properties
     * @throws IllegalArgumentException If the given foreignKeys is missing a necessary foreign key
     * for this entry, or contains a foreign key that is not necessary for this entry
     */
    abstract fun toJson(foreignKeys: Map<String, Int> = mapOf()): JSONObject

    /**
     * Saves this entry into this entry's table, at the row with this entry's id; If the id is
     * null, creates a new row in the table; If the id is not null, updates the existing row in the
     * table with this entry's information
     *
     * @param foreignKeys The foreign keys of this database entry
     * @throws IllegalArgumentException If the table does not contain a row corresponding to this
     * entry's id; If the given foreignKeys is missing a necessary foreign key for this entry, or
     * contains a foreign key that is not necessary for this entry
     */
    fun save(foreignKeys: Map<String, Int> = mapOf()) {
        val json = toJson(foreignKeys)
        // TODO: add table, id, created at, and updated at to json
        // TODO: implement http post request sender
    }
}