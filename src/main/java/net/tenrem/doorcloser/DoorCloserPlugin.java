package net.tenrem.doorcloser;

import org.bukkit.plugin.java.JavaPlugin;

public class DoorCloserPlugin extends JavaPlugin 
{
	@Override
	public void onEnable() 
	{	
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
		// removal of commands and events is done automatically by Bukkit/Spigot
		
		//getLogger().info("onDisable has been invoked!");
	}


}
