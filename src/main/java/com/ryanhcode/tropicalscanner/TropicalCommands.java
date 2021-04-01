package com.ryanhcode.tropicalscanner;

import com.ryanhcode.tropicalscanner.scan.TropicalScanner;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import static com.ryanhcode.tropicalscanner.scan.TropicalScanner.error;
import static com.ryanhcode.tropicalscanner.scan.TropicalScanner.msg;

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
            msg("Proper commands: help, scan, config, key, h, worldscan");
            return;
        }
        if(args[0].equals("scan")){
            TropicalScanner.scan();
            return;
        }
        if(args[0].equals("key")){
            if(args.length ==2){
                ModData.instance.key = args[1];
                ModData.save();
                msg("Key set");
            }else{
                error("Invalid key");
            }
            return;
        }
        if(args[0].equals("worldscan")){
            TropicalScanner.worldScan();
            return;
        }
        if(args[0].equals("h")){
            if(args.length ==2){
                ModData.instance.webHookCode = args[1];
                ModData.save();
                msg("Hook set");
            }else{
                error("Invalid hook");
            }
            return;
        }
        if(args[0].equals("ch")){
                ModData.instance.scannedCommandables.clear();
                ModData.save();
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