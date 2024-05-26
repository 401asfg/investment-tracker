package com.example.investmenttracker.model

import org.json.JSONObject

/**
 * Can be written to json
 */
interface Writable {
    /**
     * @return A json object containing this class' relevant properties
     */
    fun toJson(): JSONObject
}