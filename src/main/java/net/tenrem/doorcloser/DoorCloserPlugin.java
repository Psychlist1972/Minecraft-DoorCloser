package net.tenrem.doorcloser;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DoorCloserPlugin extends JavaPlugin 
{
	private static DoorCloserPlugin _instance;
	
	
	@Override
	public void onEnable() 
	{
		_instance = this;
		
		Settings.ThisPlugin = this;
		
		Settings.ReadConfigValues();
	
		// set up commands
		RegisterCommands();
		
		// register event listener
		RegisterEvents();
	}

	
	private void RegisterEvents()
	{
		getServer().getPluginManager().registerEvents(new InteractListener(this), this);
		
	}
	
	private void RegisterCommands()
	{
		getCommand("dcreload").setExecutor(new CommandReload(this));
	}
	
	
	
	
	@Override
	public void onDisable() 
	{
		//getLogger().info("onDisable has been invoked!");
	}
	
	public static DoorCloserPlugin getInstance() 
	{
		return _instance;
	}

}
