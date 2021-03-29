package com.ryanhcode.tropicalscanner;

import net.minecraft.stats.Achievement;

public class Exotic {
    public String name;
    public String commandableGUID;
    public String hex;
    public int color;
    public long ends;
    public int price;
    public boolean bin;

    public Exotic(String name, String commandableGUID, String hex, int color, long ends, int price, boolean bin) {
        this.name = name;
        this.commandableGUID = commandableGUID;
        this.hex = hex;
        this.color = color;
        this.ends = ends;
        this.price = price;
        this.bin = bin;
    }
}
