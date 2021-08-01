package com.ryanhcode.tropicalscanner

import com.google.gson.*
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.err
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.msg
import java.io.*
import java.net.URL
import java.net.HttpURLConnection
import java.util.zip.GZIPInputStream


/**
 * Handler for API connections and reading JSON
 */
class ConnectionHandler {

    /**
     * Reads an endpoint and parses as JSON
     *
     * @param endpoint Endpoint relative to the Hypixel API
     * @return A JsonObject of the parsed result
     */
    fun readJSON(endpoint: String): JsonElement {
        val url = URL(endpoint)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.setRequestProperty("Accept-Encoding", "gzip")
        val reader: Reader = if ("gzip" == con.contentEncoding) {
            InputStreamReader(GZIPInputStream(con.inputStream))
        } else {
            InputStreamReader(con.inputStream)
        }

        try {
            val parsed = JsonParser().parse(reader)
            if(parsed.isJsonObject) return parsed.asJsonObject
            return parsed
        } catch (e: JsonParseException) {
            msg("Error caught during JSON parsing from \"$endpoint\"")
        } catch (e: JsonSyntaxException) {
            msg("Error caught in JSON syntax from \"$endpoint\"")
        }
        return JsonObject()
    }

    fun hypixelJSON(endpoint: String): JsonObject {
        val readJSON = JsonObject()
        try {
            return readJSON(endpoint).asJsonObject
        } catch(e: IOException) {
            e.printStackTrace()
            err("Error caught in API request, are you getting rate limited, is hypixel API down, or is your API key incorrect?")
        }
        if(readJSON.has("success") && !readJSON.getBoolean("success")) {
            err("API request returned success false, are you getting rate limited, is hypixel API down, or is your API key incorrect?")
        }
        return readJSON
    }

    val endpoint = "https://api.tropicalarmor.com/graphql"
    fun graphql(query: String): JsonObject {
        val exoticString: String =
            "{\"query\":\"${query}\"}"

        val postData = exoticString.toByteArray(charset("utf-8"))

        val myurl = URL(endpoint)
        val con = myurl.openConnection() as HttpURLConnection

        con.doOutput = true
        con.requestMethod = "POST"
        con.setRequestProperty("Content-Type", "application/json")

        DataOutputStream(con.outputStream).use({ wr -> wr.write(postData) })


        val iS: InputStream = if (con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            con.getInputStream()
        } else {
            /* error from server */
            val err = con.getErrorStream()


            var content: StringBuilder = StringBuilder()

            BufferedReader(
                InputStreamReader(err)).use { br ->
                var line = ""
                while (br.readLine().also { if(it !== null) line = it } != null) {
                    content.append(line)
                    content.append(System.lineSeparator())
                }
            }

            println("Error sending query")
            println(content)



            return JsonObject()
        }

        var content: StringBuilder = StringBuilder()

        BufferedReader(
            InputStreamReader(iS)).use { br ->
            var line = ""
            while (br.readLine().also { if(it !== null) line = it } != null) {
                content.append(line)
                content.append(System.lineSeparator())
            }
        }


        try {
            val ret = JsonParser().parse(content.toString()).asJsonObject["data"]
            if(ret.isJsonObject) {
                return ret.asJsonObject
            } else {
                return JsonParser().parse(content.toString()).asJsonObject
            }

        } catch(e: Exception){
            println("Error parsing graphql result")
            e.printStackTrace()
            return JsonObject()
        }
    }
}