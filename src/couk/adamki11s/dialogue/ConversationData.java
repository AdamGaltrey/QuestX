package couk.adamki11s.dialogue;

import org.bukkit.entity.Player;

import com.topcat.npclib.entity.HumanNPC;

import couk.adamki11s.npcs.SimpleNPC;

public class ConversationData {

	Player p;
	SimpleNPC npc;

	public ConversationData(Player p, SimpleNPC npc) {
		this.p = p;
		this.npc = npc;
	}

	public Player getPlayer() {
		return p;
	}

	public SimpleNPC getSimpleNpc() {
		return npc;
	}

}
