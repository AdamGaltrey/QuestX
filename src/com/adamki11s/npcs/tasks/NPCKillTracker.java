package com.adamki11s.npcs.tasks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import com.adamki11s.questx.QuestX;

public class NPCKillTracker {

	HashSet<String> required = new HashSet<String>();
	HashSet<String> current = new HashSet<String>();

	public NPCKillTracker(String in) {
		this.parseInput(in);
	}

	// format entity_type:kills,
	void parseInput(String in) {
		String[] ents = in.split(",");
		for (String parse : ents) {
			this.required.add(parse);
			QuestX.logMSG("Loaded NPC : " + parse);
		}
	}

	public void trackKill(String npcName) {
		if (this.required.contains(npcName)) {
			if (!this.current.contains(npcName)) {
				this.current.add(npcName);
			}
		}
	}

	public boolean areRequiredNPCSKilled() {
		for(String r : this.required){
			if(!this.current.contains(r)){
				return false;
			}
		}
		return true;
	}

	public String sendNPCSToKill() {
		StringBuilder buff = new StringBuilder();
		buff.append(ChatColor.RED).append("Kill : ");
		for (String req : this.required) {
			if(!this.current.contains(req)){
				buff.append(req).append(", ");
			}
		}
		return buff.toString();
	}

}
