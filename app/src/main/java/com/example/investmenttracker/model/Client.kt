package com.example.investmenttracker.model

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

// TODO: write tests

/**
 * A client for communicating with a server
 *
 * @param serverUrl The url of the server
 */
class Client(val serverUrl: String) {
    companion object {
        /**
         * @param json The json to convert to a request body
         * @return A request body
         */
        private fun toRequestBody(json: JSONObject): RequestBody = json.toString().toRequestBody()
    }

    private val client = OkHttpClient()

    /**
     * Sends a HTTP post request with the given json, that has a null top level id, to the database
     * server
     *
     * @param table The name of the table to make the http request to
     * @param json The body of the http request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun post(table: String, json: JSONObject): JSONObject {
        val request = Request.Builder()
            .url("$serverUrl/$table")
            .post(toRequestBody(json))
            .build()

        return send(request)
    }

    /**
     * Sends a HTTP put request with the given json, that has a non null top level id, to the
     * database server
     *
     * @param table The name of the table to make the http request to
     * @param id The id of the table row to make the http request to
     * @param json The body of the http request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun put(table: String, id: Int, json: JSONObject): JSONObject {
        val request = Request.Builder()
            .url("$serverUrl/$table/$id")
            .put(toRequestBody(json))
            .build()

        return send(request)
    }

    /**
     * Sends a HTTP get request with the given params to the database server
     *
     * @param table The name of the table to make the http request to
     * @param id The id of the table row to make the http request to
     * @param params The parameters to send in the http request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun get(table: String, id: Int? = null, params: Map<String, String> = mapOf()): JSONObject
        = get(id, table, params)

    /**
     * Sends a HTTP get request with the given params to the database server
     *
     * @param table The name of the table to make the http request to
     * @param params The parameters to send in the http request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun get(table: String, params: Map<String, String>): JSONObject = get(null, table, params)

    /**
     * Sends a HTTP get request with the given params to the database server
     *
     * @param id The id of the table row to make the http request to
     * @param params The parameters to send in the http request
     * @param table The name of the table to make the http request to
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    private fun get(id: Int?, table: String, params: Map<String, String>): JSONObject {
        val urlToTable = "$serverUrl/$table"
        val idRoute = if (id !== null) "/$id" else ""

        val paramString = params.entries.fold("") { acc, entry ->
            if (acc == "") "?${entry.key}=${entry.value}"
            else "$acc&${entry.key}=${entry.value}"
        }

        val request = Request.Builder()
            .url("$urlToTable$idRoute$paramString")
            .build()

        return send(request)
    }

    /**
     * Sends the given http request to the server
     *
     * @param request The http request to send
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    private fun send(request: Request): JSONObject
        = client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            else JSONObject(response.body.toString())
        }
}