package com.ryanhcode.tropicalscanner

import com.mojang.realmsclient.gui.ChatFormatting
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.color
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.err
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.msg
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.queue
import com.ryanhcode.tropicalscanner.TropicalScanner.Companion.storage
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.command.CommandException




class TropicalCommands : CommandBase() {
    override fun getCommandName(): String? {
        return "tropical"
    }

    override fun getCommandUsage(sender: ICommandSender?): String? {
        return "/tropical <scanplayer|queuelength|toggleautoscan|key>"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender?, args: Array<String?>) {
        if (args.size == 0) {
            err("Improper usage.")
            err("Commands: key, scanplayer, queuelength, toggleautoscan")
            return
        }
        when(args[0]) {
            "scanplayer" -> {
                if(args.size == 2) {
                    args[1]?.let { queue.add(PlayerScanJob(it, "")) }
                    msg("Added \"${args[1]}\" to your scan queue")
                }else {
                    err("Improper usage. Example: /tropical scanplayer PLAYER_NAME")
                }
                return
            }
            "queuelength" -> {
                msg("Queue is ${queue.size()} items long.")
                return
            }
            "key" -> {
                if(args.size == 2) {
                    args[1]?.let {
                        storage.apiKey = it
                        storage.save()
                        msg("Set your API key!")
                    }
                }else {
                    err("Improper usage. Example: /tropical key <API KEY>")
                }
                return
            }
            "toggleautoscan" -> {
                storage.autoScan = !storage.autoScan
                msg("Set autoScan to ${color(ChatFormatting.GREEN)}${storage.autoScan}")
                storage.save()
            }
            else -> {
                err("Improper usage.")
                err("Commands: key, scanplayer, queuelength, toggleautoscan")
                return
            }
        }
        // processCommand(sender, arrayOfNulls(0))
    }
}
