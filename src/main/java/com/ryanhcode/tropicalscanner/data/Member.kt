package com.ryanhcode.tropicalscanner.data

import com.google.gson.JsonObject
import com.ryanhcode.tropicalscanner.data.*
import com.ryanhcode.tropicalscanner.data.item.*
import com.ryanhcode.tropicalscanner.getJsonObject

/**
* Represents a Hypixel Skyblock member
* @param json A JsonObject to construct this member from
*/
@Suppress("MemberVisibilityCanBePrivate")
class Member(val json: JsonObject) {
    // Inventories
    val inventories = mutableListOf<Inventory>()
    val enderChest =    inventory("ender_chest","ender_chest_contents")
    val inventory =     inventory("inventory","inv_contents")
    val armor =         inventory("armor","inv_armor")
    val wardrobe =      inventory("wardrobe","wardrobe_contents")
    val vault =         inventory("vault","personal_vault_contents")
    val storage: Map<String, Inventory>? =
            if(json.has("backpack_contents"))
                getBackpackContents()
            else
                mapOf<String, Inventory>()

    fun getBackpackContents(): Map<String, Inventory> {
        val map = mutableMapOf<String, Inventory>()
        json.get("backpack_contents").asJsonObject.entrySet().forEach {
            map.put(it.key, Inventory(it.value.asJsonObject))
        }
        return map.toMap()
    }

    init {
        storage?.forEach {
            inventories += it.value
        }
    }


    fun inventory(name: String, path: String): Inventory? {
        if(json.has(path)) {
            val inv = Inventory(name, json.getJsonObject(path))
            inventories.add(inv)
            return inv
        }
        return null
    }


}
