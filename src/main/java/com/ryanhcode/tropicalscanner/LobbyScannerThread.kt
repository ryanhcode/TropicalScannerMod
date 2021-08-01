package com.ryanhcode.tropicalscanner

import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.err
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.isInHypixel
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.isInSkyblock
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.mc
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.queue

class LobbyScannerThread: Thread() {
    override fun run() {
        try {
            while (!interrupted()) {
                if(mc().thePlayer != null && mc().thePlayer.worldObj != null && mc().thePlayer.worldObj.playerEntities != null && isInHypixel() && isInSkyblock()) {
                    val players = mc().thePlayer.worldObj.playerEntities

                    players.forEach {
                        try {
                            if(it != null) {
                                queue.add(PlayerScanJob(it.name, it.uniqueID.toString()))
                            }
                        }catch(e: Exception) {

                        }
                    }
                }
                sleep(60*1000)
            }
        }catch(e: Exception) {
            err("Lobby scanner thread died.")
        }
    }
}
