package net.tenrem.doorcloser;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.*;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.flags.registry.*;


public class RegionHandler 
{
    public static DoorCloserPlugin ThisPlugin;


    public static final String DoorCloserStateFlag_key = "door-closer-state";
    public static final boolean DoorCloserStateFlag_default = true;

    public static final StateFlag DoorCloserStateFlag = new StateFlag(DoorCloserStateFlag_key, DoorCloserStateFlag_default);


    // this needs to be called by the OnLoad handler for the plugin
    public static void RegisterFlags()
    {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

            try
            {
                registry.register(DoorCloserStateFlag);
            }
            catch (FlagConflictException e)
            {
                // flag registered by a different plugin

                ThisPlugin.getLogger().severe("The WorldGuard flag '" + DoorCloserStateFlag_key + "' has already been registered by another plugin.");
            }
    } 

    public static boolean IsPluginBehaviorEnabledHere(Block block)
    {
        // TODO:
        // Check settings to see if we're going by region list or flags
        // Given that, return true if the door closers should operate
        // on this block.
        // note that regions need to include *all* blocks that make up the door
        // because we're passing in the clicked one, not necessarily the bottom block

        return false;
    }
}