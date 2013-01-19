package com.adamki11s.dialogue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.adamki11s.npcs.SimpleNPC;
import com.topcat.npclib.entity.HumanNPC;


public class ConversationData {

	String p;
	SimpleNPC npc;

	public ConversationData(String p, SimpleNPC npc) {
		this.p = p;
		this.npc = npc;
	}

	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(p);
	}

	public SimpleNPC getSimpleNpc() {
		return npc;
	}

}
