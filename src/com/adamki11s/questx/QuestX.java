package com.adamki11s.questx;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.adamki11s.commands.QuestXCommands;
import com.adamki11s.events.ConversationRegister;
import com.adamki11s.events.EntityDeathMonitor;
import com.adamki11s.events.MovementMonitor;
import com.adamki11s.events.NPCDamageEvent;
import com.adamki11s.events.NPCInteractEvent;
import com.adamki11s.io.InitialSetup;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.io.CreateNPC;
import com.adamki11s.npcs.loading.FixedLoadingTable;
import com.adamki11s.threads.ThreadController;


public class QuestX extends JavaPlugin {

	static final Logger log = Logger.getLogger("QuestX");

	NPCHandler handle;
	ThreadController tControl;

	NPCDamageEvent npcDamageEvent;
	NPCInteractEvent npcInteractEvent;
	ConversationRegister playerChatEvent;
	MovementMonitor playerMoveEvent;
	EntityDeathMonitor entityDeathMonitorEvent;

	public static Plugin p;
		
	public static final void logMSG(String msg){
		log.info("[QuestX] " + msg);
	}
	
	public static final void logChat(Player p, String message){
		p.sendMessage(ChatColor.AQUA + "[QuestX] " + ChatColor.RESET + message);
	}

	public NPCHandler getNPCHandler() {
		return this.handle;
	}

	@Override
	public void onEnable() {
		handle = new NPCHandler(this);

		InitialSetup.run();
		
		FixedLoadingTable.spawnFixedNPCS(handle);

		
		p = this;
		this.getCommand("QuestX").setExecutor(new QuestXCommands(this));
		this.tControl = new ThreadController(handle);
		this.tControl.initiateAsyncThread(10L);

		// register events

		npcDamageEvent = new NPCDamageEvent(this, handle);
		npcInteractEvent = new NPCInteractEvent(this, handle);
		playerChatEvent = new ConversationRegister(this, handle);
		playerMoveEvent = new MovementMonitor(this, handle);
		entityDeathMonitorEvent = new EntityDeathMonitor(this);
		// register events

		/*String name = "Adam";

		boolean unique = CreateNPC.isNameUnique(name);*/

		/*if (unique) {

			CreateNPC create = new CreateNPC(name, ChatColor.RED);

			// Format id:data:quantity:chance(out of 10,000)/
			String invDrops = "1,0,5,6000#2,0,3,3000", gear = "0,0,0,0,0";
			create.setProperties(true, true, false, true, (20 * 5), (20 * 20), 15, (20 * 30), 30, 2, 2, invDrops, gear);
			create.createNPCFiles();

		} else {
			System.out.println("[QuestX] An NPC with this name already exists!");
		}*/

		// Updater updater = new Updater(this, "bukkitdev_slug", this.getFile(),
		// Updater.UpdateType.DEFAULT, false);//Final boolean = show dl progress
	}

	@Override
	public void onDisable() {
		tControl.terminateAsyncThread();
		tControl.terminateSyncronousThread();
	}

}
