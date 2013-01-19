package com.adamki11s.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class NameTagEvent implements Listener {

	boolean tagAPI = false;

	static HashMap<String, ChatColor> nameColour = new HashMap<String, ChatColor>();

	public NameTagEvent(Plugin main) {
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("TagAPI")) {
			Bukkit.getServer().getPluginManager().registerEvents(this, main);
			tagAPI = true;
		} else
			tagAPI = false;
	}

	public boolean canUseTagAPI() {
		return this.tagAPI;
	}

	public static void setBandit(String name) {
		nameColour.put(name, ChatColor.RED);
	}

	public static void setNeutral(String name) {
		if (nameColour.containsKey(name)) {
			nameColour.remove(name);
		}
	}

	public void setLawman(String name) {
		nameColour.put(name, ChatColor.GREEN);
	}

	@EventHandler
	public void onNameTag(PlayerReceiveNameTagEvent evt) {
		for (Map.Entry<String, ChatColor> entry : nameColour.entrySet()) {
			if (evt.getPlayer().getName().equalsIgnoreCase(entry.getKey())) {
				evt.setTag(entry.getValue() + entry.getKey());
			}
		}
	}

}
