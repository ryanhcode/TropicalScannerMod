package com.ryanhcode.tropicalscanner;

import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import scala.collection.parallel.ParIterableLike;

@Mod(modid = TropicalScanner.MODID, version = TropicalScanner.VERSION)
public class TropicalScanner
{

    public static KeyBinding menu;
    public static final String MODID = "tropicalscanner";
    public static final String VERSION = "1.0";

    public static void scan() {
        if(ModData.instance.showScan) {
            msg("Starting scan...");
            new ScannerThread().start();
        }

    }

    public static boolean needsToOpenConfig = false, midScan = false;
    public static void openConfig() {
        needsToOpenConfig = true;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModData.load();
        menu = new KeyBinding("Menu", Keyboard.KEY_N, "Tropical Scanner");
        ClientRegistry.registerKeyBinding(menu);
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new TropicalCommands());
    }

    public static int ticks = 0;
    @SubscribeEvent
    public void tick(TickEvent event){
        ticks++;
        if(needsToOpenConfig){
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen());
            needsToOpenConfig = false;
        }

    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event){
        if(menu.isPressed()){
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen());
        }
    }


    public static boolean isInHypixel() {
        final Minecraft mc = Minecraft.getMinecraft();
        return mc != null && mc.theWorld != null && !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel");
    }

    public static boolean isInSkyblock() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
            final ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            return scoreboardObj.getName().toLowerCase().contains("skyblock");
        }
        return false;
    }

    public static void msg(String msg){
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("" + ChatFormatting.PREFIX_CODE + ChatFormatting.LIGHT_PURPLE.getChar() + "[TropicalScanner] " + ChatFormatting.PREFIX_CODE + ChatFormatting.GREEN.GREEN.getChar() + msg));
    }

    public static void warning(String msg){
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("" + ChatFormatting.PREFIX_CODE + ChatFormatting.LIGHT_PURPLE.getChar() + "[TropicalScanner] " + ChatFormatting.PREFIX_CODE + ChatFormatting.GOLD.getChar() + "WARNING: " + msg));
    }

    public static void error(String msg){
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("" + ChatFormatting.PREFIX_CODE + ChatFormatting.LIGHT_PURPLE.getChar() + "[TropicalScanner] " + ChatFormatting.PREFIX_CODE + ChatFormatting.RED.getChar() + "ERROR: " + msg));
    }
}
