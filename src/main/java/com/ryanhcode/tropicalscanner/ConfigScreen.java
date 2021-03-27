package com.ryanhcode.tropicalscanner;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionsRowList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;
import java.util.UUID;

public class ConfigScreen extends GuiScreen {


    GuiCheckBox doAutoScan;
    GuiSlider autoScanTimer;
    GuiCheckBox showErrors;
    GuiCheckBox showWarnings;
    GuiCheckBox showScan;

    @Override
    public void initGui() {
        doAutoScan = new GuiCheckBox(101, width / 2 - 210, 40, "Do auto scan", ModData.instance.doAutoScan);
        autoScanTimer = new GuiSlider(101, width / 2 - 210, 60, 440, 20, "Auto Scan Timer: ", " minutes", 3, 60, ModData.instance.autoScanMinutes, false, true);
        int step = 120;
        showErrors = new GuiCheckBox(101, width / 2 - 210 + step + step, 40, "Show errors", ModData.instance.showErrors);
        showWarnings = new GuiCheckBox(101, width / 2 - 210 + step, 40, "Show warnings", ModData.instance.showWarnings);
        showScan = new GuiCheckBox(101, width / 2 - 210 + step + step + step, 40, "Show scan", ModData.instance.showScan);
        this.buttonList.add(doAutoScan);
        this.buttonList.add(showWarnings);
        this.buttonList.add(showErrors);
        this.buttonList.add(showScan);
        this.buttonList.add(autoScanTimer);
    }

    @Override
    public void onGuiClosed() {
        ModData.instance.doAutoScan = doAutoScan.isChecked();
        ModData.instance.showScan = showScan.isChecked();
        ModData.instance.autoScanMinutes = autoScanTimer.getValueInt();
        ModData.instance.showErrors = showErrors.isChecked();
        ModData.instance.showWarnings = showWarnings.isChecked();
        ModData.save();
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Tropical Scanner Config", width / 2, 15, Color.WHITE.getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}