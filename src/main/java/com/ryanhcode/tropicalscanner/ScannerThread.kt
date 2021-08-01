package com.ryanhcode.tropicalscanner

import com.google.gson.JsonObject
import com.mojang.realmsclient.gui.ChatFormatting
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.color
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.connectionHandler
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.err
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.mc
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.msg
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.newline
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.queue
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.storage
import com.ryanhcode.tropicalscanner.data.Profile
import java.util.*

class ScannerThread: Thread() {
    override fun run() {
        try {
            runScanner()
        } catch(e: InterruptedException) {
            err("The scanner thread somehow encountered an error that it couldn't handle- if you don't know why you should ask in the Tropical discord about this.")
        }
        err("The scanner thread somehow encountered an error- if you don't know why you should ask in the Tropical discord about this.")
    }

    private fun runScanner() {
        while(!interrupted()) {


            if(queue.hasJob()) {
                val job = queue.next()

                try {
                    fufill(job)
                }catch(e: Exception) {
                    println("Error fufilling job")
                    e.printStackTrace()
                }

                sleep(500)
            }


        }
    }

    val cache = mutableMapOf<UUID, String>()

    val scannedCache = mutableListOf<String>()

    fun fufill(job: PlayerScanJob) {
        val username = job.username
        val uuid = job.uuid

        if(storage.apiKey == "") {
            err("Woah! Tropical just tried scanning someone but you don't have your api key set.")
            err("Use /tropical key <API KEY> to set your key.")
            return
        }



        val rawProfiles = connectionHandler.hypixelJSON("https://api.hypixel.net/skyblock/profiles?key=${storage.apiKey}&uuid=${job.solveUUID()}")

        if(rawProfiles.equals(JsonObject())) {
            err("Dropped job with username ${job.username}")
            return
        }
        val profiles = rawProfiles.getArray("profiles")
        val profs = profiles.map { Profile(it.asJsonObject) }.toList()

        profs.forEach { profile ->
            try {
                profile.scan { uuid, member, inv, item ->
                    if (!scannedCache.contains(item.uuid().toString())) {


                        if (!mc().thePlayer.uniqueID.toString().equals(uuid)) {
                            scannedCache.add(item.uuid().toString())
                        }


                        if (item.colorable() && item.exotic()) {

                            val itemUUID = item.uuid()
                            val coopName =
                                if (cache.containsKey(uuid)) {
                                    cache.get(uuid)
                                } else {
                                    val mojang = connectionHandler.readJSON("https://api.mojang.com/user/profiles/${
                                        uuid.toString().replace("-", "")
                                    }/names").getAsJsonArray()
                                    mojang.get(mojang.size() - 1).getAsJsonObject().getAsJsonPrimitive("name")
                                        .getAsString();
                                }

                            val matchingItems =
                                connectionHandler.graphql("\\n{\\n  items(item_uuid:\\\"${itemUUID}\\\") {\\n    ownerName\\n    selling\\n    itemUuid\\n  }\\n}")
                            val items = matchingItems["items"]
                            if ((items != null && items.isJsonArray && items.asJsonArray.size() > 0) && !items.isJsonNull) {
                                val itemsArr = items.asJsonArray
                                if (itemsArr[0].asJsonObject["selling"].asString == "SELLING") {
                                    newline()
                                    msg("${color(ChatFormatting.GREEN)}${coopName} is selling their ${item.name()}${
                                        color(ChatFormatting.RESET)
                                    }${color(ChatFormatting.GREEN)}, check it out on Tropical!")
                                    newline()
                                }
                            } else {
                                newline()
                                msg("${color(ChatFormatting.GREEN)}${coopName} owns an unscanned exotic!")
                                msg("${item.name()}${color(ChatFormatting.RESET)} #${item.hexColor()} in their ${inv.name}.")
                                newline()
                            }
                            sleep(500)
                            println(connectionHandler.graphql("\\nmutation {\\n  postItem(itemType: \\\"${item.id()}\\\"\\nitemUuid: \\\"${item.uuid()}\\\"\\nitemColor: \\\"#${item.hexColor()}\\\"\\nownerUuid: \\\"${
                                uuid.toString().replace("-", "")
                            }\\\")\\n}"))


                        }


                    }


                }
            }catch(e: Exception) {
                err("Error scanning profile")
                e.printStackTrace()
            }
        }

    }
}
