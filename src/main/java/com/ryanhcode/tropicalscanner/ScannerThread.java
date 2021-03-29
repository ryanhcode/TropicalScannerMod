package com.ryanhcode.tropicalscanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NamedTag;
import org.apache.logging.log4j.Marker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ScannerThread extends Thread {
    private List<JsonObject> pages = new ArrayList();

    public ScannerThread(){
    }

    int totalPages = 100;
    int curPage = 0;

    public static ArrayList<String> scannedCommandables = new ArrayList();
    public static List<String> crystalArmorColors = new ArrayList<String>(Arrays.asList("1F0030", "46085E", "54146E", "5D1C78", "63237D", "6A2C82", "7E4196", "8E51A6", "9C64B3", "A875BD", "B88BC9", "C6A3D4", "D9C1E3", "E5D1ED", "EFE1F5", "FCF3FF"));
    public static List<String> fairyArmorColors = new ArrayList<String>(Arrays.asList("660066", "660033", "99004C", "CC0066", "FF007F", "FF3399", "FF66B2", "FF99CC", "FFCCE5", "FF99CC", "FF66B2", "FF3399", "FF007F", "CC0066", "99004C", "660033", "660066", "990099", "CC00CC", "FF00FF", "FF33FF", "FF66FF", "FF99FF", "FFCCFF", "E5CCFF", "CC99FF", "B266FF", "9933FF", "7F00FF", "6600CC", "4C0099", "330066", "4C0099", "6600CC", "7F00FF", "9933FF", "B266FF", "CC99FF", "E5CCFF", "FFCCFF", "FF99FF", "FF66FF", "FF33FF", "FF00FF", "CC00CC", "990099"));
    public static Map<String, String> skyblockColors = new HashMap<String, String>() {{

        put("ranchers_boots", "CC5500");
        put("squid_boots", "000000");
        put("farmer_boots", "CC5500");
        put("music_pants", "04CFD3");
        put("obsidian_chestplate", "000000");
        put("holy_dragon_boots", "47D147");
        put("holy_dragon_leggings", "47D147");
        put("superior_dragon_boots", "F25D18");
        put("holy_dragon_chestplate", "47D147");
        put("superior_dragon_leggings", "F2DF11");
        put("superior_dragon_chestplate", "F2DF11");
        put("unstable_dragon_boots", "B212E3");
        put("unstable_dragon_leggings", "B212E3");
        put("strong_dragon_leggings", "E09419");
        put("unstable_dragon_chestplate", "B212E3");
        put("unstable_dragon_helmet", "B212E3");
        put("strong_dragon_boots", "F0D124");
        put("strong_dragon_chestplate", "D91E41");
        put("protector_dragon_boots", "99978B");
        put("protector_dragon_leggings", "99978B");
        put("wise_dragon_leggings", "29F0E9");
        put("protector_dragon_chestplate", "99978B");
        put("wise_dragon_boots", "29F0E9");
        put("wise_dragon_chestplate", "29F0E9");
        put("old_dragon_boots", "F0E6AA");
        put("old_dragon_leggings", "F0E6AA");
        put("old_dragon_chestplate", "F0E6AA");
        put("young_dragon_boots", "DDE4F0");
        put("young_dragon_leggings", "DDE4F0");
        put("elegant_tuxedo_leggings", "FEFDFC");
        put("young_dragon_chestplate", "DDE4F0");
        put("elegant_tuxedo_boots", "191919");
        put("elegant_tuxedo_chestplate", "191919");
        put("fancy_tuxedo_chestplate", "332A2A");
        put("fancy_tuxedo_boots", "332A2A");
        put("fancy_tuxedo_leggings", "D4D4D4");
        put("werewolf_boots", "1D1105");
        put("werewolf_leggings", "1D1105");
        put("shark_scale_helmet", "002CA6");
        put("werewolf_chestplate", "1D1105");
        put("bat_person_boots", "000000");
        put("bat_person_chestplate", "000000");
        put("shark_scale_boots", "002CA6");
        put("shark_scale_leggings", "002CA6");
        put("shark_scale_chestplate", "002CA6");
        put("bat_person_leggings", "000000");
        put("glacite_boots", "03FCF8");
        put("mineral_leggings", "CCE5FF");
        put("glacite_leggings", "03FCF8");
        put("glacite_chestplate", "03FCF8");
        put("mineral_boots", "CCE5FF");
        put("snow_suit_leggings", "FFFFFF");
        put("mineral_chestplate", "CCE5FF");
        put("snow_suit_boots", "FFFFFF");
        put("snow_suit_chestplate", "FFFFFF");
        put("spooky_leggings", "606060");
        put("spooky_boots", "606060");
        put("spooky_chestplate", "606060");
        put("tarantula_leggings", "000000");
        put("tarantula_boots", "000000");
        put("sponge_chestplate", "FFDC51");
        put("tarantula_chestplate", "000000");
        put("tarantula_helmet", "000000");
        put("sponge_leggings", "FFDC51");
        put("sponge_boots", "FFDC51");
        put("speedster_boots", "E0FCF7");
        put("cheap_tuxedo_boots", "383838");
        put("speedster_leggings", "E0FCF7");
        put("speedster_chestplate", "E0FCF7");
        put("speedster_helmet", "E0FCF7");
        put("cheap_tuxedo_leggings", "C7C7C7");
        put("frozen_blaze_chestplate", "A0DAEF");
        put("frozen_blaze_boots", "A0DAEF");
        put("frozen_blaze_leggings", "A0DAEF");
        put("frozen_blaze_helmet", "A0DAEF");
        put("cheap_tuxedo_chestplate", "383838");
        put("blaze_boots", "F7DA33");
        put("blaze_leggings", "F7DA33");
        put("emerald_armor_boots", "00FF00");
        put("emerald_armor_leggings", "00FF00");
        put("emerald_armor_chestplate", "00FF00");
        put("blaze_chestplate", "F7DA33");
        put("chestplate_of_the_pack", "FF0000");
        put("armor_of_magma_leggings", "FF9300");
        put("emerald_armor_helmet", "00FF00");
        put("armor_of_magma_chestplate", "FF9300");
        put("armor_of_magma_boots", "FF9300");
        put("armor_of_magma_helmet", "FF9300");
        put("salmon_leggings_new", "A82B76");
        put("creeper_leggings", "7AE82C");
        put("guardian_chestplate", "117391");
        put("helmet_of_the_pack", "FFFFFF");
        put("salmon_boots_new", "C13C0F");
        put("salmon_helmet_new", "C13C0F");
        put("growth_boots", "00BE00");
        put("salmon_boots", "C13C0F");
        put("salmon_chestplate_new", "A82B76");
        put("salmon_helmet", "C13C0F");
        put("salmon_chestplate", "A82B76");
        put("salmon_leggings", "A82B76");
        put("growth_chestplate", "00BE00");
        put("growth_leggings", "00BE00");
        put("lapis_armor_helmet", "0000FF");
        put("farm_armor_chestplate", "FFD700");
        put("farm_armor_boots", "FFD700");
        put("farm_armor_leggings", "FFD700");
        put("growth_helmet", "00BE00");
        put("farm_armor_helmet", "FFD700");
        put("miner_outfit_boots", "7A7964");
        put("miner_outfit_leggings", "7A7964");
        put("miner_outfit_chestplate", "7A7964");
        put("lapis_armor_boots", "0000FF");
        put("miner_outfit_helmet", "7A7964");
        put("lapis_armor_chestplate", "0000FF");
        put("leaflet_leggings", "4DCC4D");
        put("lapis_armor_leggings", "0000FF");
        put("leaflet_chestplate", "4DCC4D");
        put("cactus_leggings", "00FF00");
        put("cactus_chestplate", "00FF00");
        put("cactus_boots", "00FF00");
        put("pumpkin_leggings", "EDAA36");
        put("leaflet_boots", "4DCC4D");
        put("cactus_helmet", "00FF00");
        put("pumpkin_boots", "EDAA36");
        put("angler_boots", "0B004F");
        put("pumpkin_chestplate", "EDAA36");
        put("pumpkin_helmet", "EDAA36");
        put("angler_leggings", "0B004F");
        put("mushroom_boots", "FF0000");
        put("angler_chestplate", "0B004F");
        put("mushroom_leggings", "FF0000");
        put("farm_suit_boots", "FFFF00");
        put("mushroom_chestplate", "FF0000");
        put("mushroom_helmet", "FF0000");
        put("farm_suit_helmet", "FFFF00");
        put("farm_suit_leggings", "FFFF00");
        put("farm_suit_chestplate", "FFFF00");
    }};

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
            TropicalScanner.msg("Scanning auction pages...");
        }
        while(!isInterrupted()) {
            try {
                sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
                TropicalScanner.isScanning = false;
                return;
            }
            if (curPage < totalPages) {
                JsonObject page = getPage(curPage);
                if(page.get("success").getAsBoolean() == false){
                    TropicalScanner.error("Error parsing auction page- key invalid?");
                    TropicalScanner.isScanning = false;
                    return;
                }
                pages.add(page);
                curPage++;
            }else{
                break;
            }
        }
        if(ModData.instance.showScan) {
            TropicalScanner.msg("Analyzing auction pages...");
        }
        for(JsonObject page : pages){
            JsonArray auctions = page.get("auctions").getAsJsonArray();
            for(JsonElement auc : auctions){
                JsonObject auction = auc.getAsJsonObject();
                String itemBytes = auction.get("item_bytes").getAsString();
                final byte[] bytes;
                try {
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
                            if (crystalArmorColors.contains(hexColor) || fairyArmorColors.contains(hexColor) || skyblockColors.get(id) == null || skyblockColors.get(id).equals(hexColor) || hexColor.equals("A06540")) {
                                continue;
                            }
                            String uuid = auction.get("auctioneer").getAsString();
                            String msg = "" + ChatFormatting.PREFIX_CODE + ChatFormatting.LIGHT_PURPLE.getChar() + "[Tropical] " + ChatFormatting.PREFIX_CODE + ChatFormatting.GREEN.GREEN.getChar() + name + ChatFormatting.PREFIX_CODE + ChatFormatting.RESET.getChar() + " $" + Math.max(auction.get("highest_bid_amount").getAsInt(), auction.get("starting_bid").getAsInt()) + " #" + hexColor;

                            IChatComponent comp = new ChatComponentText(msg);
                            String aucUUIDF = auction.get("uuid").getAsString();

                            String finalAucUUID = java.util.UUID.fromString(
                                    aucUUIDF
                                            .replaceFirst(
                                                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                                            )
                            ).toString();
                            if(scannedCommandables.contains(finalAucUUID)){
                                continue;
                            }
                            scannedCommandables.add(finalAucUUID);

                            ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + finalAucUUID));
                            comp.setChatStyle(style);

                            boolean bin = false;
                            if(auction.has("bin") && auction.get("bin").getAsBoolean()){
                                bin = true;
                            }
                            //Minecraft.getMinecraft().thePlayer.addChatMessage(comp);
                            Exotic exotic = new Exotic(
                                    name,
                                    finalAucUUID,
                                    hexColor,
                                    getIntFromColor(red, green, blue),
                                    auction.get("end").getAsLong(),
                                    Math.max(auction.get("highest_bid_amount").getAsInt(), auction.get("starting_bid").getAsInt()),
                                    bin

                            );
                            ExoticViewer.exotics.add(exotic);

                            if(!ModData.instance.scannedCommandables.contains(finalAucUUID)){
                                DiscordWebhook.sendAuctionExotic(exotic);
                                ModData.instance.scannedCommandables.add(finalAucUUID);
                            }

                        }




                    } catch (Exception exception) {
                        //lmao
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    TropicalScanner.error("Error occured parsing auction item bytes.");
                    return;
                }
            }

        }
        if(ModData.instance.showScan) {
            TropicalScanner.msg("Scan complete");
        }
        TropicalScanner.isScanning = false;
        ModData.save();
    }
    public int getIntFromColor(int red, int green, int blue){
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }
}
