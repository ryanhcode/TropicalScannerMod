package com.ryanhcode.tropicalscanner.data

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*
import com.ryanhcode.tropicalscanner.data.*
import com.ryanhcode.tropicalscanner.data.item.Inventory
import com.ryanhcode.tropicalscanner.data.item.InventoryItem
import com.ryanhcode.tropicalscanner.getJsonObject
import com.ryanhcode.tropicalscanner.getString


/**
 * Represents a Hypixel Skyblock Profile
 * @param json A JsonObject to construct this profile from
 */
class Profile(val json: JsonObject) {
    val cuteName: String    = json.getString("cute_name")
    val members             = getMembersMap()

    fun getMembersMap(): Map<String, Member> {
        val map = mutableMapOf<String, Member>()
        json.get("members").asJsonObject.entrySet().forEach {
            map.put(it.key, Member(it.value.asJsonObject))
        }
        return map.toMap()
    }

    inline fun scan(crossinline iterator: (uuid: UUID, member: Member, inv: Inventory, item: InventoryItem) -> Unit) {
        members.forEach { uuid, member ->
            member.inventories.forEach { inv ->
                inv.forEveryItem(backpacks = true) { item ->
                    iterator(uuid.toUUID(), member, inv, item)
                }
            }
        }
    }

    override fun toString(): String {
        return "Profile(\n" +
                "\tcuteName='$cuteName', \n" +
                "\tmembers=$members, \n" +
                ")"
    }


}