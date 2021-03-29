package com.ryanhcode.tropicalscanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraft.util.JsonUtils;
import org.lwjgl.Sys;

import java.io.*;
import java.util.ArrayList;

public class ModData {
    public static ModData instance;
    public String key = "", webHookCode = "";
    public ArrayList<String> scannedCommandables = new ArrayList();
    public ArrayList<String> scanned = new ArrayList();

    public int autoScanMinutes = 20, worldScanMinutes = 20;
    public boolean doAutoScan = false, showWarnings = true, showErrors = true, showScan = true, doAuctionHook = false, doWorldHook = false, doWorldScan;

    public static void load(){
        File file = new File("./config/tropicalscanner/config.json");
        if(!file.exists()){
            try {
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                Gson gson = new Gson();
                ModData src = new ModData();
                String s = gson.toJson(src);
                instance = src;
                FileWriter myWriter = new FileWriter(file.getPath());
                myWriter.write(s);
                myWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Gson gson = new Gson();
            JsonReader reader = null;
            try {
                reader = new JsonReader(new FileReader(file.getPath()));
                instance = gson.fromJson(reader, ModData.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
    public static void save(){
        File file = new File("./config/tropicalscanner/config.json");

            try {
                file.createNewFile();
                Gson gson = new Gson();
                String s = gson.toJson(instance);
                FileWriter myWriter = new FileWriter(file.getPath());
                myWriter.write(s);
                myWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public ModData(){
        System.out.println("ModData Loaded.");
    }
}
