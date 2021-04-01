package com.ryanhcode.tropicalscanner.config

import club.sk1er.vigilance.Vigilant
import club.sk1er.vigilance.data.Property
import club.sk1er.vigilance.data.PropertyType.*
import java.io.File


class VigilantConfig : Vigilant(File("./config/tropicalscanner/newconfig.toml")) {
    @Property(
        type = TEXT,
        name = "API Key",
        description = "Hypixel API Key. Needed for world scanning and player scanning.",
        category = "General",
        subcategory = "Authentication"
    )
    var apiKey = ""

    @Property(
        type = TEXT,
        name = "Webhook URL",
        description = "Webhook URL. Go to Channel Settings > Integrations > New Webhook and copy the URL here",
        category = "General",
        subcategory = "Authentication"
    )
    var webhook = ""


    @Property(
        type = SWITCH,
        name = "Do auto auction scan",
        description = "Scan the auction house automatically",
        category = "Scans",
        subcategory = "Auction Scanning"
    )
    var ahScan: Boolean = false



    @Property(
        type = SLIDER,
        min = 2,
        max = 60,
        increment = 1,
        name = "Auction scan delay",
        description = "Delay between auction scans",
        category = "Scans",
        subcategory = "Auction Scanning"
    )
    var ahScanMinutes = 10


    @Property(
        type = BUTTON,
        name = "Force kill",
        description = "Force stop an auction scan",
        category = "Scans",
        subcategory = "Auction Scanning"
    )
    fun cancelA(){

    }


    @Property(
        type = SWITCH,
        name = "Do auto world scan",
        description = "Scan the lobby you are in automatically",
        category = "Scans",
        subcategory = "World Scanning"
    )
    var worldScan: Boolean = false


    @Property(
        type = SLIDER,
        min = 1,
        max = 30,
        increment = 1,
        name = "World scan delay",
        description = "Delay between world scans",
        category = "Scans",
        subcategory = "World Scanning"
    )
    var worldScanMinutes = 10

    @Property(
        type = BUTTON,
        name = "Force kill",
        description = "Force kill a world scan",
        category = "Scans",
        subcategory = "World Scanning"
    )
    fun cancelW(){

    }

    init{
        apiKey = ""
        webhook = ""
    }

    public fun webhookCode(): String {
        return webhook.replace("https://discord.com/api/webhooks/", "")
    }
}