package com.ryanhcode.tropicalscanner;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.sun.javafx.cursor.StandardCursorFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
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
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import scala.collection.parallel.ParIterableLike;

import java.util.Set;

@Mod(modid = TropicalScanner.MODID, clientSideOnly = true, version = TropicalScanner.VERSION)
public class TropicalScanner
{

    public static KeyBinding menu, recordBook;
    public static final String MODID = "tropicalscanner";
    public static final String VERSION = "1.0";

    public static boolean isScanning;
    public static boolean isScanningWorld;
    public static void scan() {
        if(isScanning){
            warning("A scan is already in progress.");
            return;
        }
        if(ModData.instance.showScan) {
            msg("Starting scan...");
            new ScannerThread().start();
        }

    }
    public static void worldScan() {
        if(isScanningWorld){
            warning("A world scan is already in progress.");
            return;
        }
        if(ModData.instance.showScan) {
            new WorldScannerThread().start();
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
        recordBook = new KeyBinding("Record Book", Keyboard.KEY_M, "Tropical Scanner");
        ClientRegistry.registerKeyBinding(menu);
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new TropicalCommands());
    }

    public static int ticks = 0;
    public static long prevMS = System.currentTimeMillis();
    public static long curMS = 0;
    public static long deltaMS = 0;
    public static long timerMS = 0, worldTimerMS = 0;
    @SubscribeEvent
    public void tick(TickEvent event){
        ticks++;
        if(needsToOpenConfig){
            Minecraft.getMinecraft().displayGuiScreen(new ConfigScreen());
            needsToOpenConfig = false;
        }


        if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld!=null) {
            curMS = System.currentTimeMillis();
            deltaMS = curMS - prevMS;

            timerMS+=deltaMS;
            worldTimerMS+=deltaMS;

            prevMS = curMS;
            //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText((ModData.instance.autoScanMinutes*60*1000) - timerMS + " left"));
            if (timerMS > (ModData.instance.autoScanMinutes*60*1000) && ModData.instance.doAutoScan && isInHypixel() && isInSkyblock()) {
                timerMS = 0;
                scan();
            }
            if (worldTimerMS > (ModData.instance.worldScanMinutes*60*1000) && ModData.instance.doWorldScan && isInHypixel() && isInSkyblock()) {
                worldTimerMS = 0;
                worldScan();
            }
        }

    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event){
        if(menu.isPressed()){
            Minecraft.getMinecraft().displayGuiScreen(new ExoticViewer());
        }
        if(recordBook.isPressed()){
            Minecraft.getMinecraft().displayGuiScreen(new RecordSearcher());
        }
    }


    public static boolean isInHypixel() {
        final Minecraft mc = Minecraft.getMinecraft();
        return mc != null && mc.theWorld != null && !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel");
    }
    private static final Set<String> SKYBLOCK_IN_ALL_LANGUAGES = Sets.newHashSet("SKYBLOCK","\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58");
    public static boolean isInSkyblock() {
        try {
            Minecraft mc = Minecraft.getMinecraft();

            if (mc != null && mc.theWorld != null && mc.thePlayer != null) {
                if (mc.isSingleplayer() || mc.thePlayer.getClientBrand() == null ||
                        !mc.thePlayer.getClientBrand().toLowerCase().contains("hypixel")) {
                    return false;
                }

                Scoreboard scoreboard = mc.theWorld.getScoreboard();
                ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
                if (sidebarObjective != null) {
                    String objectiveName = sidebarObjective.getDisplayName().replaceAll("(?i)\\u00A7.", "");
                    for (String skyblock : SKYBLOCK_IN_ALL_LANGUAGES) {
                        if (objectiveName.startsWith(skyblock)) {
                            return true;
                        }
                    }
                }
            }
        }catch(Exception e){
            return false;
        }

        return false;
    }

    public static void msg(String msg){
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("" + ChatFormatting.PREFIX_CODE + ChatFormatting.LIGHT_PURPLE.getChar() + "[Tropical] " + ChatFormatting.PREFIX_CODE + ChatFormatting.GREEN.GREEN.getChar() + msg));
    }

    public static void warning(String msg){
        if(!ModData.instance.showWarnings){return;}
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("" + ChatFormatting.PREFIX_CODE + ChatFormatting.LIGHT_PURPLE.getChar() + "[Tropical] " + ChatFormatting.PREFIX_CODE + ChatFormatting.GOLD.getChar() + "WARNING: " + msg));
    }

    public static void error(String msg){
        if(ModData.instance.showErrors) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("" + ChatFormatting.PREFIX_CODE + ChatFormatting.LIGHT_PURPLE.getChar() + "[Tropical] " + ChatFormatting.PREFIX_CODE + ChatFormatting.RED.getChar() + "ERROR: " + msg));
        }
    }
}
