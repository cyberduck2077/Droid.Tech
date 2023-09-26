package ru.data.common.network.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser.parseString
import com.google.gson.JsonSyntaxException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.plugins.logging.Logger


suspend inline fun <reified T> HttpClient.postRequest(
    urlString: String, body: T
) = post(urlString) { setBody(body) }

suspend inline fun <reified T> HttpClient.getRequest(
    urlString: String, body: T
) = get(urlString) { setBody(body) }

suspend inline fun <reified T> HttpClient.putRequest(
    urlString: String, body: T
) = put(urlString) { setBody(body) }

suspend inline fun <reified T> HttpClient.deleteRequest(
    urlString: String, body: T
) = delete(urlString) { setBody(body) }

internal val loggerPretty = object : Logger {
    private val BODY_START = "BODY START"
    private val BODY_END = "BODY END"
    private val LOG_NAME = "HTTP Client"
    override fun log(message: String) {
        val startBody = message.indexOf(BODY_START)
        val endBody = message.indexOf(BODY_END)
        if (startBody != -1 && endBody != -1) {
            try {
                val header = message.substring(0, startBody)
                val jsonBody = message.substring(startBody + BODY_START.length, endBody)
                val prettyPrintJson = GsonBuilder().setPrettyPrinting()
                    .create().toJson(parseString(jsonBody))
                println(LOG_NAME + "$header\n$prettyPrintJson")
            } catch (m: JsonSyntaxException) {
                println(LOG_NAME + message)
            }
        } else {
            println(LOG_NAME + message)
        }
    }
}

