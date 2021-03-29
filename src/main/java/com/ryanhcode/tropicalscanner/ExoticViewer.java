package com.ryanhcode.tropicalscanner;


import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class ExoticViewer extends GuiScreen {
    public static List<Exotic> exotics = new ArrayList();
    int page = 0;
    int perPage = 0;
    GuiButton prev, next, clearAll;
    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        labelList.clear();
        prev = new GuiButton(400, width / 2 - 130-40, height-20, 40, 20, "<");
        next = new GuiButton(400, width / 2 + 130, height-20, 40, 20, ">");
        clearAll = new GuiButton(400, width / 2 - 30, height-20, 60, 20, "Clear All");
        buttonList.add(prev);
        buttonList.add(next);
        buttonList.add(clearAll);

        ArrayList<Exotic> exo = new ArrayList(exotics);
        for (Exotic exotic : exo) {
            if(System.currentTimeMillis() > exotic.ends){
                exotics.remove(exotic);
            }
        }

        int total = 0;
        int fcounter = 0;
        for(int i = 15+20; i < height - 10; i+=20){
            fcounter++;
            total = fcounter;
        }


        int exoCounter = page*total;
        int counter = 0;
        if(exoCounter >= exotics.size() && page > 0){
            page-=1;
            exoCounter = page*total;

        }
        for(int i = 15+20; i < height - 30; i+=20){
            if(exoCounter >= exotics.size()){
                continue;
            }
            Exotic exotic = exotics.get(exoCounter);
            GuiButton e = new GuiButton(100 + counter, width / 2 - 130, i, 130 * 2, 20, exotic.name + ChatFormatting.PREFIX_CODE + ChatFormatting.RESET.getChar() + ChatFormatting.PREFIX_CODE + ChatFormatting.GOLD.getChar() + " $" + exotic.price +
                    (exotic.bin ? "[B]" : "[A]"));
            GuiButton d = new GuiButton(100 + counter, width / 2 - 130-40, i, 40, 20, "x");

            GuiLabel label;
            labelList.add(label = new GuiLabel(fontRendererObj, 400+counter, width / 2 + 135, i, 100, 20, exotics.get(exoCounter).color));
            label.func_175202_a("#" + exotics.get(exoCounter).hex);
            //"#" + exotics.get(exoCounter).hex
            mainBTNs.put(e, exotic);
            delBTNs.put(d, exotic);
            buttonList.add(e);
            buttonList.add(d);

            //drawCenteredString(fontRendererObj, "Button: " + counter + "/" + total, width / 2, i, Color.WHITE.getRGB());
            counter++;
            //total = counter;
            exoCounter++;

        }
    }

    Map<GuiButton, Exotic> mainBTNs = new HashMap();
    Map<GuiButton, Exotic> delBTNs = new HashMap();

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button == prev){
            page = page - 1;
            if(page < 0){
                page = 0;
            }
            initGui();

        }
        if(button == next){
            page = page + 1;
            if(page < 0){
                page = 0;
            }
            initGui();

        }

        if(button == clearAll){
            exotics.clear();
            initGui();
        }

        Exotic exotic = delBTNs.get(button);
        if(exotic !=null){
            exotics.remove(exotic);
            initGui();
        }

        Exotic mexotic = mainBTNs.get(button);
        if(mexotic !=null){
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/viewauction " + mexotic.commandableGUID);
            TropicalScanner.msg("Opening auction...");
            //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("/viewauction " + mexotic.commandableGUID));
            onGuiClosed();
            //initGui();
        }
        super.actionPerformed(button);
    }

    public ExoticViewer(){
        labelList.clear();
        buttonList.clear();


        perPage = height/20;
    }

    int total = 0;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Tropical Exotics", width / 2, 15, Color.WHITE.getRGB());
        drawString(fontRendererObj, "Tropical by RyanHCode#1268", 5, 5, Color.WHITE.getRGB());


        super.drawScreen(mouseX, mouseY, partialTicks);


    }

}