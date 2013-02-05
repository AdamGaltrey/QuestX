package com.adamki11s.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.ai.RandomMovement;
import com.adamki11s.data.ItemStackDrop;
import com.adamki11s.data.ItemStackProbability;
import com.adamki11s.display.FixedSpawnsDisplay;
import com.adamki11s.display.Pages;
import com.adamki11s.display.QuestDisplay;
import com.adamki11s.display.StaticStrings;
import com.adamki11s.display.TaskDisplay;
import com.adamki11s.guidance.LocationGuider;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.npcs.UniqueNameRegister;
import com.adamki11s.npcs.io.CreateNPC;
import com.adamki11s.npcs.loading.FixedLoadingTable;
import com.adamki11s.npcs.tasks.Fireworks;
import com.adamki11s.npcs.tasks.TaskRegister;
import com.adamki11s.quests.QuestManager;
import com.adamki11s.quests.setup.QuestSetup;
import com.adamki11s.quests.setup.QuestUnpacker;
import com.adamki11s.questx.QuestX;
import com.adamki11s.reputation.Reputation;
import com.adamki11s.updates.Updater;
import com.adamki11s.updates.Updater.UpdateResult;
import com.adamki11s.updates.Updater.UpdateType;
import com.adamki11s.utils.FileUtils;
import com.topcat.npclib.entity.HumanNPC;

public class QuestXCommands implements CommandExecutor {

	QuestX plugin;
	NPCHandler handle;

	public QuestXCommands(QuestX main) {
		this.plugin = main;
		this.handle = main.getNPCHandler();
	}

	HumanNPC test;
	RandomMovement rm;

	HashMap<String, QuestSetup> setups = new HashMap<String, QuestSetup>();

	Pages npcList;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("questx") || label.equalsIgnoreCase("q")) {
			if (!(sender instanceof Player)) {
				System.out.println("QuestX Commands must be issued in-game.");
				return true;
			} else {
				Player p = (Player) sender;

				ItemStack[] gear = new ItemStack[] { null, null, null, null, new ItemStack(Material.WOOD_AXE) };

				if (args.length == 1 && args[0].equalsIgnoreCase("force-update")) {
					Updater u = new Updater(QuestX.p, "questx", QuestX.f, UpdateType.DEFAULT, true);
					if (u.getResult() == UpdateResult.SUCCESS) {
						QuestX.logChat(p, "QuestX version " + u.getLatestVersionString() + " was updated successfully!");
					} else {
						QuestX.logChat(p, ChatColor.RED + "Something went wrong downloading the update! Result = " + u.getResult().toString());
					}
					return true;
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("qinfo")) {
					if (args[1].equalsIgnoreCase("current")) {
						QuestDisplay.displayCurrentQuestInfo(p);
					}
					return true;
				}

				/*
				 * NPC Commands (START)
				 */

				if (args.length >= 2 && args[0].equalsIgnoreCase("npc") && args[1].equalsIgnoreCase("list")) {
					String[] list = new String[handle.getNPCs().size()];
					int count = 0;
					for (SimpleNPC npc : handle.getNPCs()) {
						list[count] = npc.getName();
						count++;
					}
					Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
					this.npcList = new Pages(list, 10);
					if (args.length == 2) {
						// page 1
						String[] send = this.npcList.getStringsToSend(1);
						QuestX.logChat(p, "Displaying (" + send.length + "/" + this.npcList.getRawArrayLength() + ") Spawned NPC's, Page (1/" + this.npcList.getPages() + ")");
						int c = 0;
						for (String s : send) {
							c++;
							QuestX.logChat(p, "#" + (c) + " - " + s);
						}
						QuestX.logChat(p, StaticStrings.separator);
						return true;
					} else if (args.length == 3) {
						int pg;
						try {
							pg = Integer.parseInt(args[2]);
							if(pg > this.npcList.getPages()){
								QuestX.logChatError(p, ChatColor.RED + "There are not that many pages!");
								return true;
							}
							String[] send = this.npcList.getStringsToSend(pg);
							QuestX.logChat(p, "Displaying (" + send.length + "/" + this.npcList.getRawArrayLength() + ") Spawned NPC's, Page (" + pg + "/" + this.npcList.getPages() + ")");
							int c = 0;
							for (String s : send) {
								c++;
								QuestX.logChat(p, "#" + ((pg * 10) + c) + " - " + s);
							}
							QuestX.logChat(p, StaticStrings.separator);
						} catch (NumberFormatException nfe) {
							QuestX.logChatError(p, ChatColor.RED + "Page number must be an integer!");
						}
						return true;
					}

					return true;
				}

				if (args.length == 3 && args[0].equalsIgnoreCase("npc") && args[1].equalsIgnoreCase("delete")) {
					String toDel = args[2];
					try {
						FileUtils.deleteDirectory(FileLocator.getNPCRootDir(toDel));
						handle.getSimpleNPCByName(toDel).destroyNPCObject();
						if (FixedLoadingTable.doesNPCHaveFixedSpawn(toDel)) {
							FixedLoadingTable.removeFixedNPCSpawn(p, toDel, handle);
						}
						QuestX.logChat(p, ChatColor.GREEN + "NPC deleted successfully.");

					} catch (IOException e) {
						QuestX.logChatError(p, ChatColor.RED + "Error while deleting NPC '" + toDel + "'");
						e.printStackTrace();
					}
				}

				/*
				 * NPC Commands (END)
				 */

				/*
				 * Task Commands (START)
				 */

				if (args.length == 2 && args[0].equalsIgnoreCase("task")) {
					if (args[1].equalsIgnoreCase("info")) {
						TaskDisplay.displayTaskInfo(p);
					} else if (args[1].equalsIgnoreCase("cancel")) {
						TaskRegister.cancelPlayerTask(p);
					}
					return true;
				}

				/*
				 * Task Commands (END)
				 */

				if (args.length == 2 && args[0].equalsIgnoreCase("unpack")) {
					String qName = args[1];
					QuestUnpacker upack = new QuestUnpacker(qName);
					boolean suc = upack.unpackQuest();
					if (suc) {
						QuestX.logChat(p, "Unpack successfull");
						QuestX.logChat(p, "/QuestX setup <questname> " + ChatColor.GREEN + " to setup this quest");
					} else {
						QuestX.logChat(p, "Error while unpacking");
					}
					return true;
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("setup")) {
					String qName = args[1];
					if (setups.containsKey(p.getName())) {
						QuestX.logChat(p, "You are already setting this quest up!");
						return true;
					}
					if (!setups.containsKey(p.getName())) {
						if (QuestManager.doesQuestExist(qName)) {
							if (!QuestManager.hasQuestBeenSetup(qName)) {
								QuestSetup qs = new QuestSetup(qName, handle);
								if (!qs.canSetup()) {
									QuestX.logChat(p, "Failed to start setup, reason : " + qs.getFailSetupReason());
								} else {
									QuestX.logChat(p, "Setup successful! /questx next " + ChatColor.GREEN + "To select the next spawn location");
									qs.sendInitialMessage(p);
									this.setups.put(p.getName(), qs);
								}
							} else {
								QuestX.logChat(p, "This quest has already been setup");
							}
						} else {
							QuestX.logChat(p, "A quest by that name does not exist");
						}
					}
					return true;
				}

				if (args.length == 1 && args[0].equalsIgnoreCase("next")) {
					if (setups.containsKey(p.getName())) {
						QuestSetup qs = this.setups.get(p.getName());
						if (!qs.isSetupComplete()) {
							qs.setupSpawn(p);
							if (qs.isSetupComplete()) {
								qs.removeFromList();
								this.setups.remove(p.getName());
								QuestX.logChat(p, "Quest setup successfully!");
							}
						}
					} else {
						QuestX.logChat(p, "You aren't setting up a quest!");
					}
					return true;
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
					if (UniqueNameRegister.isNameUnique(args[1])) {
						CreateNPC create = new CreateNPC(args[1], ChatColor.RED);

						// Format id:data:quantity:chance(out of 10,000)/
						String invDrops = "1,0,5,6000#2,0,3,3000", gr = "0,0,0,0,0";
						create.setProperties(true, true, false, true, (20 * 5), (20 * 20), 15, (20 * 30), 30, 2, 2, invDrops, gr);
						create.createNPCFiles();
					} else {
						QuestX.logChat(p, "NPC with this name already exists");
					}
					return true;
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("find")) {
					String npcName = args[1];
					SimpleNPC npc = this.handle.getSimpleNPCByName(npcName);
					if (npc == null) {
						QuestX.logChat(p, "NPC with this name is not spawned");
						return true;
					} else {
						Location npcLoc = npc.getHumanNPC().getBukkitEntity().getLocation();
						LocationGuider guide = new LocationGuider(p.getName(), npcLoc.getWorld().getName(), npcLoc.getBlockX(), npcLoc.getBlockY(), npcLoc.getBlockZ());
						guide.drawPath();
						/*
						 * Fireworks f = new
						 * Fireworks(npc.getHumanNPC().getBukkitEntity
						 * ().getLocation(), 6, 20); f.fireLocatorBeacons();
						 * QuestX.logChat(p, "Launching locator beacons!");
						 */
						return true;
					}
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("tele")) {
					String npcName = args[1];
					SimpleNPC npc = this.handle.getSimpleNPCByName(npcName);
					if (npc == null) {
						QuestX.logChat(p, "An NPC with this name has not spawned");
						return true;
					} else {
						p.teleport(npc.getHumanNPC().getBukkitEntity().getLocation());
						QuestX.logChat(p, "Teleported to NPC '" + npc + "'.");
						return true;
					}
				}

				if (args.length == 3 && args[0].equalsIgnoreCase("fixedspawns")) {
					String npcName = args[2];
					if (args[1].equalsIgnoreCase("remove")) {
						FixedLoadingTable.removeFixedNPCSpawn(p, npcName, handle);
						return true;
					} else if (args[1].equalsIgnoreCase("edit")) {
						FixedLoadingTable.editFixedNPCSpawn(p, npcName, handle);
						return true;
					} else if (args[1].equalsIgnoreCase("removeall")) {
						if (p.isOp()) {
							FixedLoadingTable.deleteAllFixedSpawns(p, handle);
						} else {
							QuestX.logChatError(p, "You must be an Operator to perform this command");
						}
						return true;
					}
				}

				if (args.length >= 2 && args[0].equalsIgnoreCase("fixedspawns")) {
					if (args[1].equalsIgnoreCase("display")) {
						if (args.length == 2) {
							FixedSpawnsDisplay.display(p, 1);
						} else if (args.length == 3) {
							int pg = 1;
							try {
								pg = Integer.parseInt(args[2]);
							} catch (NumberFormatException nfe) {
								QuestX.logChat(p, ChatColor.RED + "Page number must be an integer! /q display fixedspawns <page>");
							}
							FixedSpawnsDisplay.display(p, pg);
						}
						return true;
					}
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("setfixedspawn")) {
					String npcName = args[1];
					boolean suc = FixedLoadingTable.addFixedNPCSpawn(p, npcName, p.getLocation(), handle);
					if (suc) {
						SimpleNPC snpc = this.handle.getSimpleNPCByName(npcName);
						if (snpc != null) {
							snpc.spawnNPC();
						}
					}
					return true;
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("stressspawn")) {
					int max = Integer.parseInt(args[1]);
					for (int i = 0; i < max; i++) { // 1/10 chance of dropping
						SimpleNPC snpc = new SimpleNPC(this.handle, ("a" + i), true, true, 60, 200, 20, 100, 200, new ItemStackDrop(new ItemStackProbability[] { new ItemStackProbability(
								new ItemStack(Material.GOLD_AXE, 1), 6000) }), gear, 1, 1.5);
						snpc.setNewSpawnLocation(p.getLocation());
						snpc.spawnNPC();
						// QuestX.logChat(p, "NPC Spawned!");
					}
					return true;
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("spawn")) {
					String npcName = args[1];
					if (!UniqueNameRegister.isNameUnique(npcName)) {
						QuestX.logChat(p, ChatColor.RED + "Name is not unique!");
						return true;
					} else {
						SimpleNPC snpc = new SimpleNPC(this.handle, npcName, true, true, 60, 200, 10, 100, 200, new ItemStackDrop(new ItemStackProbability[] { new ItemStackProbability(
								new ItemStack(Material.GOLD_AXE, 1), 6000) }), gear, 1, 1.5);
						snpc.spawnNPC();

						QuestX.logChat(p, "NPC Spawned!");
						return true;
					}

				}
			}
		}
		return true;
	}
}
