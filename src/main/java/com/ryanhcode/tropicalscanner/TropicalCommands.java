package com.ryanhcode.tropicalscanner;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import static com.ryanhcode.tropicalscanner.TropicalScanner.error;
import static com.ryanhcode.tropicalscanner.TropicalScanner.msg;

public class TropicalCommands extends CommandBase {

    @Override
    public String getCommandName() {
        return "tropical";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tropical <help|scan|config>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0){
            error("Improper usage.");
            msg("Proper commands: help, scan, config");
            return;
        }
        if(args[0].equals("scan")){
            TropicalScanner.scan();
            return;
        }
        if(args[0].equals("config")){
            TropicalScanner.openConfig();
            msg("Opening config...");
            return;
        }
        processCommand(sender, new String[0]);
    }

}