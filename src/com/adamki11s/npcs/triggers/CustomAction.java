package com.adamki11s.npcs.triggers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.triggers.action.Action;
import com.adamki11s.npcs.triggers.action.DamagePlayerAction;
import com.adamki11s.npcs.triggers.action.ExecuteCMDAction;
import com.adamki11s.npcs.triggers.action.InvokeQuestAction;
import com.adamki11s.npcs.triggers.action.InvokeTaskAction;
import com.adamki11s.npcs.triggers.action.LightningAction;
import com.adamki11s.npcs.triggers.action.MobSpawnAction;
import com.adamki11s.npcs.triggers.action.NPCAttackPlayerAction;
import com.adamki11s.npcs.triggers.action.PlayRecordAction;
import com.adamki11s.npcs.triggers.action.PlayerGiveItemsAction;
import com.adamki11s.npcs.triggers.action.PotionEffectAction;
import com.adamki11s.npcs.triggers.action.TeleportAction;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class CustomAction {

	final String npcName;
	final File f;

	private ArrayList<Action> actions = new ArrayList<Action>();

	boolean invokesTorQ = false, isTask = false;

	private Action invoke;

	// auto ends conversation using this trigger

	public CustomAction(String npcName, File f) {
		this.npcName = npcName;
		this.f = f;
	}
	
	public String getFileName(){
		return f.getName();
	}

	public void invokeActions(Player p) {
		if (this.actions.size() == 0) {
			return;
		}

		if (invokesTorQ) {
			if (isTask) {
				InvokeTaskAction a = (InvokeTaskAction) invoke;
				if (!a.canPlayerTriggerTask(p)) {
					return;
				}
			} else {
				InvokeQuestAction a = (InvokeQuestAction) invoke;
				if (!a.canPlayerTriggerQuest(p)) {
					return;
				}
			}
		}

		for (Action a : actions) {
			if (a.isActive()) {
				a.implement(p);
			}
		}
	}

	public void load(NPCHandler handle) {
		SyncConfiguration io = new SyncConfiguration(f);

		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			io.read();

			if (io.doesKeyExist("DAMAGE_PLAYER")) {
				Action a = new DamagePlayerAction(npcName, io.getString("DAMAGE_PLAYER"));
				if (a.isActive()) {
					actions.add(a);
				}
			}

			if (io.doesKeyExist("INVOKE_QUEST")) {
				Action a = new InvokeQuestAction(handle, io.getString("INVOKE_QUEST"));
				if (a.isActive()) {
					invokesTorQ = true;
					isTask = false;
					invoke = a;
				}
			}

			if (io.doesKeyExist("INVOKE_TASK")) {
				Action a = new InvokeTaskAction(npcName);
				if (a.isActive()) {
					invokesTorQ = true;
					isTask = true;
					invoke = a;
				}
			}

			if (io.doesKeyExist("LIGHTNING")) {
				Action a = new LightningAction(npcName, io.getString("LIGHTNING"));
				if (a.isActive()) {
					actions.add(a);
				}
			}

			if (io.doesKeyExist("SPAWN_MOBS") && io.doesKeyExist("SPAWN_MOB_RANGE") && io.doesKeyExist("SPAWN_COOLDOWN_MINUTES") && io.doesKeyExist("DESPAWN_MOB_SECONDS")
					&& io.doesKeyExist("MOBS_TARGET_PLAYER")) {
				String[] data = new String[5];
				data[0] = io.getString("SPAWN_MOBS");
				data[1] = io.getString("SPAWN_MOB_RANGE");
				data[2] = io.getString("SPAWN_COOLDOWN_MINUTES");
				data[3] = io.getString("DESPAWN_MOB_SECONDS");
				data[4] = io.getString("MOBS_TARGET_PLAYER");

				Action a = new MobSpawnAction(npcName, data);
				if (a.isActive()) {
					actions.add(a);
				}
			}

			if (io.doesKeyExist("ATTACK_PLAYER")) {
				Action a = new NPCAttackPlayerAction(handle, npcName, io.getString("ATTACK_PLAYER"));
				if (a.isActive()) {
					actions.add(a);
				}
			}

			if (io.doesKeyExist("PLAYER_GIVE_ITEMS") && io.doesKeyExist("GIVE_ITEMS_COOLDOWN_MINUTES")) {
				Action a = new PlayerGiveItemsAction(npcName, io.getString("PLAYER_GIVE_ITEMS"), io.getInt("GIVE_ITEMS_COOLDOWN_MINUTES"));
				if (a.isActive()) {
					actions.add(a);
				}
			}

			if (io.doesKeyExist("POTION_EFFECT")) {
				Action a = new PotionEffectAction(npcName, io.getString("POTION_EFFECT"));
				if (a.isActive()) {
					actions.add(a);
				}
			}

			if (io.doesKeyExist("TELEPORT_PLAYER")) {
				Action a = new TeleportAction(npcName, io.getString("TELEPORT_PLAYER"));
				if (a.isActive()) {
					actions.add(a);
				}
			}

			// PLAY_RECORD:<record block id>,<range>

			if (io.doesKeyExist("PLAY_RECORD")) {
				Action a = new PlayRecordAction(npcName, io.getString("PLAY_RECORD"));
				if (a.isActive()) {
					actions.add(a);
				}
			}

			// EXECUTE_PLAYER_CMD:command 1#command 2
			// EXECUTE_SERVER_CMD:command 1#command 2

			if (io.doesKeyExist("EXECUTE_PLAYER_CMD")) {
				// active always return true for cmds
				Action a = new ExecuteCMDAction(io.getString("EXECUTE_PLAYER_CMD"), true);
				actions.add(a);
			}
			
			if (io.doesKeyExist("EXECUTE_SERVER_CMD")) {
				// active always return true for cmds
				Action a = new ExecuteCMDAction(io.getString("EXECUTE_SERVER_CMD"), false);
				actions.add(a);
			}

		}
		/*
		 * Properties
		 * 
		 * SET_VELOCITY:x,y,z
		 */
	}

}
