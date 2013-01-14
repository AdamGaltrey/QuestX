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
				this.retalliate(npc.getAggressor(), npc);
			}
		}
	}
	
	synchronized void retalliate(Player p, SimpleNPC npc){
		//if(p is inside npc area) then attack
		
		npc.moveTo(p.getLocation());
		npc.lookAt(p.getLocation());
		if(npc.getHumanNPC().getBukkitEntity().getLocation().distance(p.getLocation()) < 2){
			p.sendMessage("NPC HIT YOU");
			npc.getHumanNPC().animateArmSwing();
			p.damage(1);
			System.out.println(p.getHealth());
		}
		
		//else if aggressor has left area stop attacking
	}

}
