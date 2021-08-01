package com.ryanhcode.tropicalscanner

import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.connectionHandler

data class PlayerScanJob(val username: String, val uuid: String) {
    fun solveUUID(): String {
        if(uuid == "") {
            return connectionHandler.readJSON("https://api.mojang.com/users/profiles/minecraft/${username}").asJsonObject.get("id").asString
        }
        return uuid;
    }
}