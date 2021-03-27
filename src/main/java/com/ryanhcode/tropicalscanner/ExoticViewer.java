package com.ryanhcode.tropicalscanner;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class ExoticViewer extends GuiScreen {
    public static List<Exotic> exotics = new ArrayList();
    int page = 0;
    int perPage = 0;

    @Override
    public void initGui() {
        super.initGui();
        labelList.clear();
        buttonList.clear();

        ArrayList<Exotic> exo = new ArrayList(exotics);
        for (Exotic exotic : exo) {
            if(System.currentTimeMillis() > exotic.ends){
                exotics.remove(exotic);
            }
        }
        List<Exotic> toDraw = this.exotics.subList(page*perPage, Math.min((page*perPage) + perPage, exotics.size()));
        int counter = 0;
        for (Exotic exotic : toDraw) {
            buttonList.add(new GuiButton(width / 2 - 100, 100, 40 + (counter * 20), 100, 20, "Show errors"));
            counter++;
        }
        perPage = (height/20) - 3;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Tropical Exotics", width / 2, 15, Color.WHITE.getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}