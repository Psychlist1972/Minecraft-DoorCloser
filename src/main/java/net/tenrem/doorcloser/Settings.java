package net.tenrem.doorcloser;

import java.util.ArrayList;
import org.bukkit.Material;
import java.util.List;

public class Settings 
{
	public static int secondsToRemainOpen = 10;
	public static boolean ignoreIfInCreative = false;
	public static boolean ignoreIfSneaking = false;
	public static boolean playSound = true;

	public static boolean synchronizeDoubleDoorOpen = true;
	public static boolean synchronizeDoubleDoorClose = true;

	public static List<Material> doorsInScope = new ArrayList<Material>();
	public static List<Material> gatesInScope = new ArrayList<Material>();
	public static List<Material> trapDoorsInScope = new ArrayList<Material>();
	
	public static DoorCloserPlugin ThisPlugin;

	
	public static void Reload()
	{
		if (ThisPlugin != null)
		{
			ThisPlugin.reloadConfig();
		
			ReadConfigValues();
			
			ThisPlugin.getLogger().info("Settings reloaded from configuration file.");
		}
	}
	
	public static void ReadConfigValues()
	{		
		if (ThisPlugin == null)
			return;
		
		// save the default config, if it's not already present
		ThisPlugin.saveDefaultConfig();		
		
		// read settings
		
		Settings.secondsToRemainOpen = ThisPlugin.getConfig().getInt("Time");
		Settings.ignoreIfInCreative = ThisPlugin.getConfig().getBoolean("IgnoreIfInCreative");
		Settings.ignoreIfSneaking = ThisPlugin.getConfig().getBoolean("IgnoreIfSneaking");
		Settings.playSound = ThisPlugin.getConfig().getBoolean("PlaySound");

		Settings.synchronizeDoubleDoorOpen = ThisPlugin.getConfig().getBoolean("SynchronizeDoubleDoorOpen");
		Settings.synchronizeDoubleDoorClose = ThisPlugin.getConfig().getBoolean("SynchronizeDoubleDoorClose");

		
		List<String> trapDoorsInScopeStrings = (List<String>) ThisPlugin.getConfig().getList("TrapDoorBlocks");
		List<String> gatesInScopeStrings = (List<String>) ThisPlugin.getConfig().getList("GateBlocks");
		List<String> doorsInScopeStrings = (List<String>) ThisPlugin.getConfig().getList("DoorBlocks");

		trapDoorsInScope.clear();
		gatesInScope.clear();
		doorsInScope.clear();
		
		for (String val : trapDoorsInScopeStrings)
		{
			Material m = Material.matchMaterial(val);	
			
			if (m != null)
			{
				Settings.trapDoorsInScope.add(m);
			}
			else
			{
				ThisPlugin.getLogger().warning("Unexpected value '" + val + "' in config trap door list.");
			}
		}
		
		for (String val : gatesInScopeStrings)
		{
			Material m = Material.matchMaterial(val);

			if (m != null)
			{
				Settings.gatesInScope.add(m);
			}
			else
			{
				ThisPlugin.getLogger().warning("Unexpected value '" + val + "' in config gate list.");
			}
		}
		
		for (String val : doorsInScopeStrings)
		{
			Material m = Material.matchMaterial(val);

			if (m != null)
			{
				Settings.doorsInScope.add(m);
			}
			else
			{
				ThisPlugin.getLogger().warning("Unexpected value '" + val + "' in config door list.");
			}
	
		}
		
		
		// log the read settings out to the server log
		ThisPlugin.getLogger().info("Seconds to remain open: " + Settings.secondsToRemainOpen);
		ThisPlugin.getLogger().info("Ignore if in creative mode: " + Settings.ignoreIfInCreative);
		ThisPlugin.getLogger().info("Ignore if sneaking: " + Settings.ignoreIfSneaking);
		ThisPlugin.getLogger().info("Play sound: " + Settings.playSound);
		
		if (Settings.trapDoorsInScope.isEmpty() && Settings.gatesInScope.isEmpty() && Settings.doorsInScope.isEmpty())
		{
			ThisPlugin.getLogger().warning("No doors, gates, or trap doors configured to auto-close. Is the config file correct?");
		}
		else
		{
			ThisPlugin.getLogger().info("Count of trap doors in scope: " + Settings.trapDoorsInScope.size());
			ThisPlugin.getLogger().info("Count of gate types in scope: " + Settings.gatesInScope.size());
			ThisPlugin.getLogger().info("Count of door types in scope: " + Settings.doorsInScope.size());
		}
				
	}
	
	
}
