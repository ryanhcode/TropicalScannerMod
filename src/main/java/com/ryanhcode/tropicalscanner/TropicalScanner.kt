package com.ryanhcode.tropicalscanner

import com.mojang.realmsclient.gui.ChatFormatting
import com.ryanhcode.tropicalscanner.TropicalScanner
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.client.ClientCommandHandler
import net.minecraft.init.Blocks
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.Mod
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.scoreboard.Scoreboard
import com.google.common.collect.Sets

@Mod(modid = TropicalScanner.MODID, clientSideOnly = true, version = TropicalScanner.VERSION)
class TropicalScanner {

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        storage = TropicalStorage.load()

        LobbyScannerThread().start()
        ScannerThread().start()

        ClientCommandHandler.instance.registerCommand(TropicalCommands())
    }



    companion object {
        const val MODID = "tropicalscanner"
        const val VERSION = "3.0"
        lateinit var storage: TropicalStorage
        val connectionHandler = ConnectionHandler()
        val queue = ScanQueue()


        fun isInHypixel(): Boolean {
            val mc = Minecraft.getMinecraft()
            return mc != null && mc.theWorld != null && !mc.isSingleplayer && mc.currentServerData.serverIP.toLowerCase()
                .contains("hypixel")
        }

        private val SKYBLOCK_IN_ALL_LANGUAGES: Set<String> =
            Sets.newHashSet("SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58")

        fun isInSkyblock(): Boolean {
            try {
                val mc = Minecraft.getMinecraft()
                if (mc != null && mc.theWorld != null && mc.thePlayer != null) {
                    if (mc.isSingleplayer || mc.thePlayer.clientBrand == null ||
                        !mc.thePlayer.clientBrand.toLowerCase().contains("hypixel")
                    ) {
                        return false
                    }
                    val scoreboard = mc.theWorld.scoreboard
                    val sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1)
                    if (sidebarObjective != null) {
                        val objectiveName: String = sidebarObjective.displayName.replace("(?i)\\u00A7.".toRegex(), "")
                        for (skyblock in SKYBLOCK_IN_ALL_LANGUAGES) {
                            if (objectiveName.startsWith(skyblock)) {
                                return true
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                return false
            }
            return false
        }


        fun mc() = Minecraft.getMinecraft()

        fun msg(msg: String) {
            if(mc().thePlayer == null) return
            mc().thePlayer.addChatMessage(
                ChatComponentText("" + ChatFormatting.PREFIX_CODE + ChatFormatting.BLUE.getChar() + "[Tropical] " + ChatFormatting.PREFIX_CODE + ChatFormatting.RESET.getChar() + msg)
            )
        }

        fun newline() {
            if(mc().thePlayer == null) return
            mc().thePlayer.addChatMessage(ChatComponentText(" "))
        }

        fun err(msg: String) {
            msg(ChatFormatting.PREFIX_CODE.toString() + ChatFormatting.RED.char + msg)
        }

        fun color(color: ChatFormatting) = ChatFormatting.PREFIX_CODE.toString() + color.char
    }
}