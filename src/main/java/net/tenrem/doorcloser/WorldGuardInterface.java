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

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.*;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.flags.registry.*;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;


public class WorldGuardInterface 
{
    public static DoorCloserPlugin ThisPlugin;


    public static final boolean DoorCloserStateFlag_default = true;

    private static StateFlag _doorCloserStateFlag = null;


    // this needs to be called by the OnLoad handler for the plugin
    public static void RegisterFlags()
    {
        String flagString = Settings.worldGuardFlag;

        _doorCloserStateFlag = new StateFlag(flagString, Settings.worldGuardFlag_Default);

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try
        {
            registry.register(_doorCloserStateFlag);
        }
        catch (FlagConflictException e)
        {
            // flag registered by a different plugin

            ThisPlugin.getLogger().severe("The WorldGuard flag '" + flagString + "' has already been registered by another plugin.");
        }

    } 

    public static boolean IsPluginBehaviorEnabledHere(Block block, Player player)
    {
        Logger logger = ThisPlugin.getLogger();
        
        WorldGuard worldGuardInstance = WorldGuard.getInstance();

        // make sure we can access worldguard
        if (worldGuardInstance == null)
        {
            logger.severe("WorldGuard integration enabled, but could not find WorldGuard plugin instance. WorldGuard integration is now disabled to prevent spamming your logs.");

            // Disable WorldGuard integration
            SharedState.WorldGuardIntegrationEnabled = false;

            return false;
        }

        // note that regions need to include *all* blocks that make up the door
        // because we're passing in the clicked one, not necessarily the bottom block

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        // need player for some API calls, even though we don't really care about the player
        LocalPlayer wrappedPlayer = getWorldGuard().wrap(player);

        // need to wrap this location with a worldedit one
        Location location = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ())

        RegionQuery query = container.createQuery();

        if (Settings.useWorldGuardFlag)
        {
            // use the region flag

            if (_doorCloserStateFlag != null)
            {
                // check the region the block is in to see if flag is set

                boolean flagValue = query.testState(location, wrappedPlayer, _doorCloserStateFlag);
            }
        }
        else
        {
            // use list in config

            //ApplicableRegionSet regions = query.getApplicableRegions(location);

            //regions.getRegions().

            // TODO: Check to see if regions contains the ones from the config file
        }

    }
}