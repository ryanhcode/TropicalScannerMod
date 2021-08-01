package com.ryanhcode.tropicalscanner

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.File
import java.io.FileWriter
import java.io.FileNotFoundException
import java.io.FileReader
import java.lang.Exception
import java.util.ArrayList


class TropicalStorage {

    var apiKey: String = ""
    var autoScan = true

    companion object {
        const val path = "./config/tropicalscanner/config.json"
        fun load(): TropicalStorage {
            val file = File(path)
            if (!file.exists()) {
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs()
                    }
                    file.createNewFile()
                    val gson = Gson()
                    val s: String = gson.toJson(this)
                    val myWriter = FileWriter(file.getPath())
                    myWriter.write(s)
                    myWriter.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                val gson = Gson()
                var reader: JsonReader? = null
                try {
                    reader = JsonReader(FileReader(file.getPath()))
                    return gson.fromJson(reader, TropicalStorage::class.java)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
            return TropicalStorage()
        }

    }

    fun save() {
        val file = File(path)
        try {
            file.createNewFile()
            val gson = Gson()
            val s: String = gson.toJson(this)
            val myWriter = FileWriter(file.getPath())
            myWriter.write(s)
            myWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}