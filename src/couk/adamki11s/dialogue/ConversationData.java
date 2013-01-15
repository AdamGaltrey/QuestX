package couk.adamki11s.dialogue;

import org.bukkit.entity.Player;

import com.topcat.npclib.entity.HumanNPC;

public class ConversationData {

	Player p;
	HumanNPC npc;

	public ConversationData(Player p, HumanNPC npc) {
		this.p = p;
		this.npc = npc;
	}

	public Player getPlayer() {
		return p;
	}

	public HumanNPC getNpc() {
		return npc;
	}

}
