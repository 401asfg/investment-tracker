package com.example.investmenttracker.model

import org.json.JSONObject
import java.io.IOException

/**
 * A client for using a RESTful protocol to communicate with a server
 */
interface RestfulClient {
    /**
     * Sends a post request with the given json, that has a null top level id, to the database
     * server
     *
     * @param table The name of the table to make the request to
     * @param json The body of the request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun post(table: String, json: JSONObject): JSONObject

    /**
     * Sends a get request with the given params to the database server
     *
     * @param table The name of the table to make the request to
     * @param id The id of the table row to make the request to
     * @param params The parameters to send in the request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun get(table: String, id: Int? = null, params: Map<String, String> = mapOf()): JSONObject

    /**
     * Sends a get request with the given params to the database server
     *
     * @param table The name of the table to make the request to
     * @param params The parameters to send in the request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun get(table: String, params: Map<String, String>): JSONObject
}