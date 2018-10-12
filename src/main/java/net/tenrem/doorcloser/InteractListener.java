package net.tenrem.doorcloser;

import org.bukkit.block.data.*;
import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.material.MaterialData;
//import org.bukkit.material.Openable;
//import org.bukkit.Material;
//import org.bukkit.block.data.type.*;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;

//import javax.lang.model.util.ElementScanner6;

import org.bukkit.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
	

	// TODO: It was asked this plugin also make it so double-doors open together. To do that, check Door.Hinge etc.
	
	// This is going to fire for every interaction, so need to exit it quickly if it's not what we want to handle
	@EventHandler(priority=EventPriority.NORMAL)
	public void blockInteract(PlayerInteractEvent e) 
	{
		Action action = e.getAction();
		 
		// right clicks only
		if (action == Action.RIGHT_CLICK_BLOCK)
		{
			Block clickedBlock = e.getClickedBlock();
			BlockData blockData = clickedBlock.getBlockData();
			//BlockState state = clickedBlock.getState();
			 
			// check to see if we care about this type of block. In our case, we want
			// something that implements Openable (gate, trap door, door).
			//if ((state != null) && (state.getData() != null) && (state.getData() instanceof Openable))
			if (blockData instanceof Openable)
			{		 
				 // check to see if we're ignoring creative mode
				if ((e.getPlayer().getGameMode() == GameMode.CREATIVE) && (Settings.ignoreIfInCreative))
				{
					_plugin.getLogger().info("DEBUG: Ignored - in creative mode");
					return;
				}
	
				 // check to see if we're ignoring sneaking
				if ((e.getPlayer().isSneaking()) && (Settings.ignoreIfSneaking))
				{
					_plugin.getLogger().info("DEBUG: Ignored - is sneaking");
					return;
				}
		      
				
				Material blockDoorType = blockData.getMaterial();
				 
				// check to see if it is a type of block we want to close. Note that
				// we're not doing any type checking on these. I don't want to have to
				// maintain a finite list of doors/gates that has to be updated with
				// each version of Minecraft.
				 
				// todo: should check to see if we were clicking it closed. If so, don't schedule a close
				
				if (blockData instanceof TrapDoor && Settings.trapDoorsInScope.contains(blockDoorType))
				{
					_plugin.getLogger().info("DEBUG: Trap door found: " + clickedBlock.getType().toString());

					ScheduleClose(clickedBlock, Settings.secondsToRemainOpen);				 
				}
				 
				else if (blockData instanceof Gate && Settings.gatesInScope.contains(blockDoorType))
				{
					_plugin.getLogger().info("DEBUG: Gate found: " + clickedBlock.getType().toString());

					ScheduleClose(clickedBlock, Settings.secondsToRemainOpen);				 
				}
				 
				else if (blockData instanceof Door && Settings.doorsInScope.contains(blockDoorType))
				{
					_plugin.getLogger().info("DEBUG: Normal door found: " + clickedBlock.getType().toString());

					// check to see if they clicked the top of the door. If so, change to the block below it.
					// Necessary because server only supports the door operations on the lower block.
					// Do this only for doors so as not to mess up stacked gates or stacked trap doors
					// This could fail if you somehow manage to get two doors stacked on top of each other.

					// todo: look at the Bisected interface 
					
					if (clickedBlock.getRelative(BlockFace.DOWN).getType().equals(blockDoorType))
					{
						clickedBlock = clickedBlock.getRelative(BlockFace.DOWN);
					}
						 					 
					ScheduleClose(clickedBlock, Settings.secondsToRemainOpen);				 			 
				}
				else
				{
					// be sure to comment this out or change log level or else it will spam the logs
					_plugin.getLogger().info("DEBUG: Unexpected block: " + blockDoorType.toString());
				}
			}
		}
	}
	 
	 

	public void ScheduleClose(Block doorBlock, int seconds)
	{		
		// Schedule the closing to happen at apx "seconds" seconds from now.
		Bukkit.getScheduler().runTaskLater(_plugin, new Runnable()
			{
				@Override
				public void run()
				{
					_plugin.getLogger().info("DEBUG: In the running / scheduled task.");

					// get current block state
					//BlockState state = block.getState();
					//BlockData data = state.getBlockData();

					BlockData data = doorBlock.getBlockData();


//					if (state != null)
//					{
//						BlockData data = state.getBlockData();
						//MaterialData data = state.getData();

						if (data != null)
						{
							if (data instanceof Openable)
							{
								Openable doorData = (Openable)data;
								
								// this is the point of the whole plugin right here.
								if (doorData.isOpen())
								{
									_plugin.getLogger().info("DEBUG: Closing open door");

									doorData.setOpen(false);
									doorBlock.setBlockData(doorData);
									
									if (Settings.playSound)
									{
										if (doorData instanceof TrapDoor)
										{
											doorBlock.getWorld().playSound(doorBlock.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 1, 1);
										}
										else if (doorData instanceof Gate)
										{
											doorBlock.getWorld().playSound(doorBlock.getLocation(), Sound.BLOCK_FENCE_GATE_CLOSE, 1, 1);

										}
										else if (doorData instanceof Door)
										{
											doorBlock.getWorld().playSound(doorBlock.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);

										}
									}
								}
								else
								{
									_plugin.getLogger().info("DEBUG: Door was closed before we got to auto-close it.");	
								}
							}
							else
							{
								// to be useful, this should probably include material information
								_plugin.getLogger().warning("Tried to close the block, but block data instanceof Openable check failed.");
							}
						}
						else
						{
							_plugin.getLogger().warning("Tried to close block, but block data was null.");
						}
//					}
				}
		
			}, (long)seconds * TICKS_PER_SECOND);

	} 
}
