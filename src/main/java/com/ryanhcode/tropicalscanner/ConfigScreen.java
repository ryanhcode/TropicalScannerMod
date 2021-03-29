package com.ryanhcode.tropicalscanner;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;

public class ConfigScreen extends GuiScreen {


    GuiSlider autoScanTimer, autoWorldScanTimer;
    GuiCheckBox showScan, doAuctionHook, doWorldHook, doWorldScan, showWarnings, doAutoScan;
    GuiTextField key, hook;

    @Override
    public void initGui() {
        doAutoScan = new GuiCheckBox(101, width / 2 - 210, 40, "Do auto scan", ModData.instance.doAutoScan);
        autoScanTimer = new GuiSlider(102, width / 2 - 210, 60, 440, 20, "Auto Scan Timer: ", " minutes", 1, 60, ModData.instance.autoScanMinutes, false, true);
        autoWorldScanTimer = new GuiSlider(102, width / 2 - 210, 85, 440, 20, "World Scan Timer: ", " minutes", 2, 10, ModData.instance.worldScanMinutes, false, true);
        int step = 120;
        showWarnings = new GuiCheckBox(104, width / 2 - 210 + step, 40, "Show warnings", ModData.instance.showWarnings);
        showScan = new GuiCheckBox(105, width / 2 - 210 + step + step + step, 40, "Show scan", ModData.instance.showScan);

        doAuctionHook = new GuiCheckBox(103, width / 2 - 210 + step + step, 85+25+25+25, "Auction hook", ModData.instance.doAuctionHook);
        doWorldHook = new GuiCheckBox(104, width / 2 - 210 + step, 85+25+25+25, "World hook", ModData.instance.doWorldHook);
        doWorldScan = new GuiCheckBox(103, width / 2 - 210 + step + step, 40,  "World scan", ModData.instance.doWorldScan);


        key = new GuiTextField(300,
                fontRendererObj, width / 2 - 210, 85+25, 440, 20);
        key.setMaxStringLength(200);
        key.setText(ModData.instance.key);



        hook = new GuiTextField(300,
                fontRendererObj, width / 2 - 210, 85+50, 440, 20);
        hook.setMaxStringLength(200);
        hook.setText(ModData.instance.webHookCode);


        this.buttonList.add(doAutoScan);
        this.buttonList.add(doWorldScan);
        this.buttonList.add(showWarnings);
        this.buttonList.add(showScan);
        this.buttonList.add(autoScanTimer);
        this.buttonList.add(autoWorldScanTimer);
        this.buttonList.add(doAuctionHook);
        this.buttonList.add(doWorldHook);
    }

    @Override
    public void onGuiClosed() {
        ModData.instance.doAutoScan = doAutoScan.isChecked();
        ModData.instance.showScan = showScan.isChecked();
        ModData.instance.autoScanMinutes = autoScanTimer.getValueInt();
        ModData.instance.showWarnings = showWarnings.isChecked();
        ModData.instance.doWorldHook = doWorldHook.isChecked();
        ModData.instance.doAuctionHook = doAuctionHook.isChecked();
        ModData.instance.worldScanMinutes = autoWorldScanTimer.getValueInt();
        ModData.instance.doWorldScan = doWorldScan.isChecked();
        ModData.save();
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Tropical Scanner Config", width / 2, 15, Color.WHITE.getRGB());
        drawString(fontRendererObj, "Tropical by RyanHCode#1268", 5, 5, Color.WHITE.getRGB());


        hook.drawTextBox();
        key.drawTextBox();
        if(key.getText().equals("")) {
            drawString(fontRendererObj, "API Key", width / 2 - 205, 91+ 25, Color.DARK_GRAY.getRGB());
        }
        if(hook.getText().equals("")) {
            drawString(fontRendererObj, "Webhook", width / 2 - 205, 91 + 25+ 25, Color.DARK_GRAY.getRGB());
        }
        ModData.instance.key = key.getText();
        ModData.instance.webHookCode = hook.getText();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.key.mouseClicked(mouseX, mouseY, mouseButton);
        this.hook.mouseClicked(mouseX, mouseY, mouseButton);

    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
        super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
        this.hook.textboxKeyTyped(p_keyTyped_1_, p_keyTyped_2_);
        this.key.textboxKeyTyped(p_keyTyped_1_, p_keyTyped_2_);
    }
}