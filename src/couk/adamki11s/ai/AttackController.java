package couk.adamki11s.ai;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.topcat.npclib.entity.HumanNPC;

import couk.adamki11s.ai.dataset.Reputation;
import couk.adamki11s.npcs.BanditNPC;
import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.npcs.SimpleNPC;

public class AttackController {

	HashMap<HumanNPC, Player> target = new HashMap<HumanNPC, Player>();

	final NPCHandler handle;

	public AttackController(NPCHandler handle) {
		this.handle = handle;
	}

	public synchronized void run() {
		for(SimpleNPC npc : handle.getNPCs()){
			if(npc.isUnderAttack()){
			}
		}
	}
	
	synchronized void decideNPCResponse(Player p, BanditNPC npc){
		Reputation r = Reputation.getPlayerReputation(p.getName());
	}

}
