package com.ryanhcode.tropicalscanner.data.item

import net.querz.nbt.io.NBTDeserializer
import net.querz.nbt.tag.ByteArrayTag
import net.querz.nbt.tag.CompoundTag
import java.io.ByteArrayInputStream
import com.ryanhcode.tropicalscanner.data.*


class InventoryItem(val tag: CompoundTag) {
    fun isBackpack(): Boolean {
        tag.getCompoundTag("ExtraAttributes")?.let { attribute ->
            attribute.entrySet().forEach {
                if("_backpack_data" in it.key) return true
            }
        }
        return false
    }

    fun backpackContents(): Inventory {
        tag.getCompoundTag("ExtraAttributes")?.let { attribute ->
            val items = mutableListOf<InventoryItem>()
            attribute.entrySet().forEach {
                if("_backpack_data" in it.key) {
                    val byt = (it.value as ByteArrayTag).value
                    val rawTag = NBTDeserializer().fromStream(ByteArrayInputStream(byt))
                    val tag = rawTag.tag as CompoundTag
                    items += InventoryItem(tag)
                }
            }
            return Inventory("backpack", items)
        }
        error("This item is not a backpack")
    }

    override fun toString() = "Item(${tag.toString()})"

    fun colorable(): Boolean {
        tag.getCompoundTag("display")?.let { it.getIntTag("color")?.let { colorTag -> return true } }
        return false
    }
    fun color(): Int? {
        tag.getCompoundTag("display")?.let { it.getIntTag("color")?.let { colorTag -> return colorTag.asInt() } }
        return null
    }
    fun id(): String? {
        tag.getCompoundTag("ExtraAttributes")?.let {
            it.getStringTag("id")?.let { idTag ->
                val id = idTag.value.toLowerCase()
                return id
            }
        }
        return null
    }
    fun uuid(): String {
        tag.getCompoundTag("ExtraAttributes")?.let {
            it.getStringTag("uuid")?.let { idTag ->
                val id = idTag.value
                return id
            }
        }
        return ""
    }

    fun name(): String {

        tag.getCompoundTag("display")?.let { it.getStringTag("Name")?.let { nameTag -> return nameTag.getValue() } }
        return "unknown name"
    }

    fun hexColor() = Integer.toHexString(color()!!)

    fun exotic() = try { SkyblockColors.isExotic(id()!!, color()!!) } catch(e: Exception) { false }

}