package net.tenrem.doorcloser;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public final class InteractListener implements Listener 
{
	// this is a bukkit / minecraft constant. Put here only for clarity
	private static final int TICKS_PER_SECOND = 20;

	private final DoorCloserPlugin _plugin;

	
	public InteractListener(DoorCloserPlugin plugin)
	{
		_plugin = plugin;
	}
	
	
	// This is going to fire for every interaction, so need to exit it quickly if it's not what we want to handle
	@EventHandler(priority=EventPriority.NORMAL)
	public void blockInteract(PlayerInteractEvent e) 
	{
		Action action = e.getAction();
		 
		// right clicks only
		if (action == Action.RIGHT_CLICK_BLOCK)
		{
			Block clickedBlock = e.getClickedBlock();
			BlockState state = clickedBlock.getState();
			 
			// check to see if we care about this type of block. In our case, we want
			// something that implements Openable (gate, trap door, door).
			if ((state != null) && (state.getData() != null) && (state.getData() instanceof Openable))
			{		 
				 // check to see if we're ignoring creative mode
				if ((e.getPlayer().getGameMode() == GameMode.CREATIVE) && (Settings.ignoreIfInCreative))
				{
					return;
				}
	
				 // check to see if we're ignoring sneaking
				if ((e.getPlayer().isSneaking()) && (Settings.ignoreIfSneaking))
				{
					return;
				}
		      
				 
				 
				// check to see if it is a type of block we want to close. Note that
				// we're not doing any type checking on these. I don't want to have to
				// maintain a finite list of doors/gates that has to be updated with
				// each version of Minecraft.
				 
				if (Settings.trapDoorsInScope.contains(clickedBlock.getType()))
				{
					ScheduleClose(clickedBlock, Settings.secondsToRemainOpen);				 
				}
				 
				else if (Settings.gatesInScope.contains(clickedBlock.getType()))
				{
					ScheduleClose(clickedBlock, Settings.secondsToRemainOpen);				 
				}
				 
				else if (Settings.doorsInScope.contains(clickedBlock.getType()))
				{
					// check to see if they clicked the top of the door. If so, change to the block below it.
					// Necessary because server only supports the door operations on the lower block.
					// Do this only for doors so as not to mess up stacked gates or stacked trap doors
					// This could fail if you somehow manage to get two doors stacked on top of each other.
					 
					if (clickedBlock.getRelative(BlockFace.DOWN).getType().equals(clickedBlock.getType()))
					{
						clickedBlock = clickedBlock.getRelative(BlockFace.DOWN);
					}
						 					 
					ScheduleClose(clickedBlock, Settings.secondsToRemainOpen);				 			 
				}
			}
		}
	}
	 
	 

	public void ScheduleClose(Block block, int seconds)
	{		
		// Schedule the closing to happen at apx "seconds" seconds from now.
		BukkitTask timer = Bukkit.getScheduler().runTaskLater(DoorCloserPlugin.getInstance(), new Runnable()
			{
				@Override
				public void run()
				{
					// get current block state
					BlockState state = block.getState();
					
					if (state != null)
					{
						MaterialData data = state.getData();

						if (data != null)
						{
							if (data instanceof Openable)
							{
								Openable door = (Openable)data;
								
								// this is the point of the whole plugin right here.
								if (door.isOpen())
								{
									door.setOpen(false);
									state.update();
								}
							}
							else
							{
								// to be useful, this should probably include material information
								_plugin.getLogger().warning("Tried to close the block, but instanceof Openable check failed.");
							}
						}
					}
				}
		
			}, (long)seconds * TICKS_PER_SECOND);

	} 
}
