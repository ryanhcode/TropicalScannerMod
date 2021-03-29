package com.ryanhcode.tropicalscanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NamedTag;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static com.ryanhcode.tropicalscanner.TropicalScanner.error;
import static com.ryanhcode.tropicalscanner.TropicalScanner.msg;

public class WorldScannerThread extends Thread {

    public WorldScannerThread(){
    }

    public static ArrayList<String> scanned = new ArrayList();


    @Override
    public void run() {

        if(ModData.instance.key.equals("")){
            TropicalScanner.warning("No API key is set");
            return;
        }

        TropicalScanner.isScanningWorld = true;
        if(ModData.instance.showScan) {
            msg("Scanning players...");
        }


        try {
            List<Entity> l = Minecraft.getMinecraft().theWorld.loadedEntityList;
            List<Entity> loadedEntityList = new ArrayList(l);
            for (final Entity entity : loadedEntityList) {
                try {
                    if (entity instanceof EntityPlayer) {
                        EntityPlayer entityPlayer = (EntityPlayer) entity;
                        if (entityPlayer != Minecraft.getMinecraft().thePlayer) {
                            String truename = ((Entity) entityPlayer).getName();
                            String uuid = entityPlayer.getUniqueID().toString().replace("-", "");
                            scanPlayer(uuid, truename);
                            sleep(1000);
                        }
                    }
                } catch (Exception e) {
                    if(e instanceof InterruptedException){
                        return;
                    }
                }

            }
        }catch(Exception e){
            if(e instanceof InterruptedException){
                return;
            }
        }



        if(ModData.instance.showScan) {
            msg("Scan complete");
        }
        TropicalScanner.isScanningWorld = false;
    }

    private void scanPlayer(String uuid, String truename) {
        JsonObject json = new JsonParser().parse(ConnectionUtils.read("https://api.hypixel.net/skyblock/profiles?key=" + ModData.instance.key + "&uuid=" + uuid)).getAsJsonObject();
        for (final JsonElement prof : json.getAsJsonArray("profiles")) {
            for (final Map.Entry<String, JsonElement> m : prof.getAsJsonObject().getAsJsonObject("members").entrySet()) {

                if(m.getValue().getAsJsonObject().getAsJsonObject("wardrobe_contents") != null){
                    String mData = m.getValue().getAsJsonObject().getAsJsonObject("wardrobe_contents").getAsJsonPrimitive("data").getAsString();
                    scan(uuid, mData, "Armor", truename, m);
                }
                if(m.getValue().getAsJsonObject().getAsJsonObject("ender_chest_contents") != null){
                    String mData = m.getValue().getAsJsonObject().getAsJsonObject("ender_chest_contents").getAsJsonPrimitive("data").getAsString();
                    scan(uuid, mData, "Armor", truename, m);
                }
                if(m.getValue().getAsJsonObject().getAsJsonObject("inv_contents") != null){
                    String mData = m.getValue().getAsJsonObject().getAsJsonObject("inv_contents").getAsJsonPrimitive("data").getAsString();
                    scan(uuid, mData, "Armor", truename, m);

                }
                if(m.getValue().getAsJsonObject().getAsJsonObject("inv_armor") != null){
                    String mData = m.getValue().getAsJsonObject().getAsJsonObject("inv_armor").getAsJsonPrimitive("data").getAsString();
                    scan(uuid, mData, "Armor", truename, m);
                }

            }
        }
    }

    private void scan(String player, String itemBytes, String from, String truename, Map.Entry<String, JsonElement> m) {
        try {
            final byte[] bytes;

            bytes = itemBytes.getBytes("UTF-8");
            final byte[] buf = Base64.getDecoder().decode(bytes);

            try {
                final ByteArrayInputStream byt = new ByteArrayInputStream(buf);
                final NamedTag result = new NBTDeserializer().fromStream((InputStream) byt);
                final JsonObject tag = new JsonParser().parse(result.getTag().valueToString()).getAsJsonObject();
                for (final JsonElement jsonElement : tag.getAsJsonObject("i").getAsJsonObject("value").getAsJsonArray("list")) {
                    final String color = jsonElement.getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").getAsJsonObject("color").getAsJsonPrimitive("value").getAsString();
                    String id = jsonElement.getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("ExtraAttributes").getAsJsonObject("value").getAsJsonObject("id").getAsJsonPrimitive("value").getAsString();
                    final String name = jsonElement.getAsJsonObject().getAsJsonObject("tag").getAsJsonObject("value").getAsJsonObject("display").getAsJsonObject("value").getAsJsonObject("Name").getAsJsonPrimitive("value").getAsString();
                    if (color == null || id == null || name == null) {
                        continue;
                    }
                    id = id.toLowerCase();
                    String[] splitColor = color.split(":");
                    int red = Integer.parseInt(splitColor[0]);
                    int green = Integer.parseInt(splitColor[1]);
                    int blue = Integer.parseInt(splitColor[2]);
                    final String hexColor = String.format("%02X%02X%02X", red, green, blue);
                    if (ScannerThread.crystalArmorColors.contains(hexColor) || ScannerThread.fairyArmorColors.contains(hexColor) || ScannerThread.skyblockColors.get(id) == null || ScannerThread.skyblockColors.get(id).equals(hexColor) || hexColor.equals("A06540")) {
                        continue;
                    }

                    final String coopUUID = m.getKey();
                    final JsonArray mojang = new JsonParser().parse(ConnectionUtils.read("https://api.mojang.com/user/profiles/" + coopUUID.replace("-", "") + "/names")).getAsJsonArray();
                    final String coopName = mojang.get(mojang.size() - 1).getAsJsonObject().getAsJsonPrimitive("name").getAsString();

                    if(scanned.contains((coopName+hexColor+id))){
                        continue;
                    }
                    scanned.add((coopName+hexColor+id));

                    if(!ModData.instance.scanned.contains(coopName + " " + from + " " + id + " " + hexColor)) {
                        ModData.instance.scanned.add(coopName + " " + from + " " + id + " " + hexColor);
                    }

                    String msg = coopName + " has " + name + ChatFormatting.PREFIX_CODE + ChatFormatting.RESET.getChar() + " #" + hexColor + " in their " +ChatFormatting.PREFIX_CODE + ChatFormatting.GREEN.getChar() + from;
                    msg(msg);

                    DiscordWebhook.sendPlayerExotic(coopName, from, getIntFromColor(red, green, blue), name, hexColor);
                }




            } catch (Exception exception) {
                //lmao
            }
        } catch (Exception e) {
            e.printStackTrace();
            TropicalScanner.error("Error occured parsing auction item bytes.");
        }
    }

    public int getIntFromColor(int red, int green, int blue){
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }
}
