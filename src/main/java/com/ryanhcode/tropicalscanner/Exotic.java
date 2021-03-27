package com.ryanhcode.tropicalscanner;

import net.minecraft.stats.Achievement;

public class Exotic {
    public String name;
    public String commandableGUID;
    public String hex;
    public int color;
    public long ends;

    public Exotic(String name, String commandableGUID, String hex, int color, long ends) {
        this.name = name;
        this.commandableGUID = commandableGUID;
        this.hex = hex;
        this.color = color;
        this.ends = ends;
    }
}
