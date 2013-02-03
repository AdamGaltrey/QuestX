package com.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import com.adamki11s.io.GeneralConfigData;
import com.adamki11s.questx.QuestX;
import com.adamki11s.reputation.ReputationManager;

public class TagColourEvent implements Listener {

	public TagColourEvent(QuestX main) {
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void onNameTag(PlayerReceiveNameTagEvent event) {
		if (QuestX.tagAPIEnabled && GeneralConfigData.isTagAPISupported()) {
			if (ReputationManager.getNamesToColour().containsKey(event.getPlayer().getName())) {
				event.setTag(ReputationManager.getNamesToColour().get(event.getPlayer().getName()) + event.getPlayer().getName());
			}
		}
	}

}
