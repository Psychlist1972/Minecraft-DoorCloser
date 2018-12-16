package net.tenrem.doorcloser;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Settings 
{
	final static String configFileGeneratedByVersion_Key = "GeneratedByVersion";

	final static String secondsToRemainOpen_Key = "Time";
	final static String synchronizeDoubleDoorOpen_Key = "SynchronizeDoubleDoorOpen";
	final static String synchronizeDoubleDoorClose_Key = "SynchronizeDoubleDoorClose1";	
	final static String playSound_Key = "PlaySound";

	final static String ignoreIfInCreative_Key = "IgnoreIfInCreative";
	final static String ignoreIfSneaking_Key = "IgnoreIfSneaking";

	final static String trapDoorsInScope_Key = "TrapDoorBlocks";
	final static String gatesInScope_Key = "GateBlocks";
	final static String doorsInScope_Key = "DoorBlocks";


	final static String configFileGeneratedByVersion_Default = "unknown (pre 1.0.12)";
	final static int secondsToRemainOpen_Default = 5;

	final static boolean synchronizeDoubleDoorOpen_Default = true;
	final static boolean synchronizeDoubleDoorClose_Default = true;
	final static boolean playSound_Default = true;

	final static boolean ignoreIfInCreative_Default = true;
	final static boolean ignoreIfSneaking_Default = false;


	public static String configFileGeneratedByVersion = configFileGeneratedByVersion_Default;

	public static int secondsToRemainOpen = secondsToRemainOpen_Default;
	public static boolean synchronizeDoubleDoorOpen = synchronizeDoubleDoorOpen_Default;
	public static boolean synchronizeDoubleDoorClose = synchronizeDoubleDoorClose_Default;
	public static boolean playSound = playSound_Default;

	public static boolean ignoreIfInCreative = ignoreIfInCreative_Default;
	public static boolean ignoreIfSneaking = ignoreIfSneaking_Default;

	public static List<Material> trapDoorsInScope = new ArrayList<Material>();
	public static List<Material> gatesInScope = new ArrayList<Material>();
	public static List<Material> doorsInScope = new ArrayList<Material>();
	
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
		
		FileConfiguration config = ThisPlugin.getConfig();

		config.addDefault(configFileGeneratedByVersion_Key, configFileGeneratedByVersion_Default);
		config.addDefault(secondsToRemainOpen_Key, secondsToRemainOpen_Default);
		config.addDefault(synchronizeDoubleDoorOpen_Key, synchronizeDoubleDoorOpen_Default);
		config.addDefault(synchronizeDoubleDoorClose_Key, synchronizeDoubleDoorClose_Default);
		config.addDefault(playSound_Key, playSound_Default);
		config.addDefault(ignoreIfInCreative_Key, ignoreIfInCreative_Default);
		config.addDefault(ignoreIfSneaking_Key, ignoreIfSneaking_Default);

		// read settings

		Settings.configFileGeneratedByVersion = ThisPlugin.getConfig().getString(configFileGeneratedByVersion_Key);

		Settings.secondsToRemainOpen = ThisPlugin.getConfig().getInt(secondsToRemainOpen_Key);

		Settings.synchronizeDoubleDoorOpen = ThisPlugin.getConfig().getBoolean(synchronizeDoubleDoorOpen_Key);
		Settings.synchronizeDoubleDoorClose = ThisPlugin.getConfig().getBoolean(synchronizeDoubleDoorClose_Key);

		Settings.playSound = ThisPlugin.getConfig().getBoolean(playSound_Key);

		Settings.ignoreIfInCreative = ThisPlugin.getConfig().getBoolean(ignoreIfInCreative_Key);
		Settings.ignoreIfSneaking = ThisPlugin.getConfig().getBoolean(ignoreIfSneaking_Key);

		// the actual blocks to interact with		
		List<String> trapDoorsInScopeStrings = (List<String>) ThisPlugin.getConfig().getStringList(trapDoorsInScope_Key);
		List<String> gatesInScopeStrings = (List<String>) ThisPlugin.getConfig().getStringList(gatesInScope_Key);
		List<String> doorsInScopeStrings = (List<String>) ThisPlugin.getConfig().getStringList(doorsInScope_Key);


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
		
		if (Settings.trapDoorsInScope.isEmpty() && Settings.gatesInScope.isEmpty() && Settings.doorsInScope.isEmpty())
		{
			ThisPlugin.getLogger().warning("No doors, gates, or trap doors configured to auto-close. Is the config file up to date?" );
			ThisPlugin.getLogger().warning("The DoorCloser plugin will still run and consume resources.");
			ThisPlugin.getLogger().warning("Update the configuration file and then use the /dcreload command to reload it.");
		}
		else
		{
			ThisPlugin.getLogger().info("Count of trap doors in scope: " + Settings.trapDoorsInScope.size());
			ThisPlugin.getLogger().info("Count of gate types in scope: " + Settings.gatesInScope.size());
			ThisPlugin.getLogger().info("Count of door types in scope: " + Settings.doorsInScope.size());
		}

		ThisPlugin.getLogger().info("Seconds to remain open: " + Settings.secondsToRemainOpen);
		ThisPlugin.getLogger().info("Ignore if in creative mode: " + Settings.ignoreIfInCreative);
		ThisPlugin.getLogger().info("Ignore if sneaking: " + Settings.ignoreIfSneaking);
		ThisPlugin.getLogger().info("Play sound: " + Settings.playSound);
		ThisPlugin.getLogger().info("Config file generated by version: " + Settings.configFileGeneratedByVersion);

	}
	
	
}
