package com.example.investmenttracker.model

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * A client for communicating with the server
 */
class Client {
    companion object {
        private const val SERVER_URL = ""

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
     * @param json The body of the http request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun post(json: JSONObject): JSONObject {
        val request = Request.Builder()
            .url(SERVER_URL)
            .post(toRequestBody(json))
            .build()

        return send(request)
    }

    /**
     * Sends a HTTP put request with the given json, that has a non null top level id, to the
     * database server
     *
     * @param json The body of the http request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun put(json: JSONObject): JSONObject {
        val request = Request.Builder()
            .url(SERVER_URL)
            .put(toRequestBody(json))
            .build()

        return send(request)
    }

    /**
     * Sends a HTTP get request with the given params to the database server
     *
     * @param params The parameters to send in the http request
     * @return The body of the response to this request
     * @throws IOException If the server rejected this request
     */
    fun get(params: Map<String, String>): JSONObject {
        val paramString = params.entries.fold("") { acc, entry ->
            "$acc&${entry.key}=${entry.value}"
        }

        val request = Request.Builder()
            .url("$SERVER_URL?$paramString")
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