package com.ryanhcode.tropicalscanner.gui;


import com.ryanhcode.tropicalscanner.ModData;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class RecordSearcher extends GuiScreen {
    GuiTextField query;

    public static String queryT = "", prevQueryT;

    @Override
    public void initGui() {
        query = new GuiTextField(300,
                fontRendererObj, width / 2 - 210, 40, 440, 20);
        query.setMaxStringLength(200);
        query.setText(queryT);
        refresh();

    }
    
    public ArrayList<String> current = new ArrayList();

    public void refresh(){
        current.clear();
        for (String s : ModData.instance.scanned) {
            if(s.toLowerCase().contains(queryT.toLowerCase())){
                current.add(new String(s));
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(1);
        drawCenteredString(fontRendererObj, "Record Book(All past exotics found via world scans)", width / 2, 15, Color.WHITE.getRGB());
        drawCenteredString(fontRendererObj, "Items from this may be very old", width / 2, 25, Color.WHITE.getRGB());
        drawString(fontRendererObj, "Tropical by RyanHCode#1268", 5, 5, Color.WHITE.getRGB());


        super.drawScreen(mouseX, mouseY, partialTicks);

        query.drawTextBox();
        if(query.getText().equals("")) {
            drawString(fontRendererObj, "Search Query", width / 2 - 205, 22+ 25, Color.DARK_GRAY.getRGB());
        }
        queryT = query.getText();
        if(queryT != prevQueryT){
            refresh();
        }
        prevQueryT = queryT;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.6,0.6,0.6);

        int xOffset = 20;
        int yOffset = 150;
        for (String cur : current) {
            drawString(fontRendererObj, cur, xOffset, yOffset, Color.WHITE.getRGB());
            yOffset += 20;
            if(yOffset > height/0.6 - 50){
                yOffset=150;
                xOffset+=300;
            }

        }
        GlStateManager.popMatrix();

    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.query.mouseClicked(mouseX, mouseY, mouseButton);

    }

    @Override
    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
        super.keyTyped(p_keyTyped_1_, p_keyTyped_2_);
        this.query.textboxKeyTyped(p_keyTyped_1_, p_keyTyped_2_);
    }
}