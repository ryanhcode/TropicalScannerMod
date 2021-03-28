package com.ryanhcode.tropicalscanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NamedTag;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static com.ryanhcode.tropicalscanner.TropicalScanner.msg;

public class WorldScannerThread extends Thread {

    public WorldScannerThread(){
    }

    int totalPages = 100;
    int curPage = 0;


    public JsonObject getPage(int page){
        try {
            JsonObject json = new JsonParser().parse(ConnectionUtils.read("https://api.hypixel.net/skyblock/auctions?page=" + page)).getAsJsonObject();
            totalPages = json.get("totalPages").getAsInt();
            return json;

        }catch(Exception e) {
            TropicalScanner.error("Error fetching auction page- do you have no internet or are you being rate limited?");
        }
        return null;
    }

    @Override
    public void run() {
        TropicalScanner.isScanning = true;
        if(ModData.instance.showScan) {
            msg("Scanning players...");
        }



        String uuid = "";
        JsonObject json = new JsonParser().parse(ConnectionUtils.read("https://api.hypixel.net/skyblock/profiles?key=" + ModData.instance.key + "&uuid=" + uuid)).getAsJsonObject();
        for (final JsonElement prof : json.getAsJsonArray("profiles")) {
            for (final Map.Entry<String, JsonElement> m : prof.getAsJsonObject().getAsJsonObject("members").entrySet()) {
                if(m.getValue().getAsJsonObject().getAsJsonObject("wardrobe_contents") != null){
                    String mData = m.getValue().getAsJsonObject().getAsJsonObject("wardrobe_contents").getAsJsonPrimitive("data").getAsString();
                    if (scan("a", mData)) return;

                }
            }
        }
        if(ModData.instance.showScan) {
            msg("Scan complete");
        }
        TropicalScanner.isScanning = false;
    }

    private boolean scan(String player, String itemBytes) {
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
                    String msg = player + " has " + name + ChatFormatting.PREFIX_CODE + ChatFormatting.RESET.getChar() + " #" + hexColor;
                    msg(msg);

                }




            } catch (Exception exception) {
                //lmao
            }
        } catch (Exception e) {
            e.printStackTrace();
            TropicalScanner.error("Error occured parsing auction item bytes.");
            return true;
        }
        return false;
    }

    public int getIntFromColor(int red, int green, int blue){
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }
}
