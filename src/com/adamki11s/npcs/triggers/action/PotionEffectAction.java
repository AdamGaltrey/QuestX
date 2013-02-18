package com.adamki11s.npcs.triggers.action;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.adamki11s.questx.QuestX;

public class PotionEffectAction implements Action {
	
	//PLAYER_POTION_EFFECT:<type>,<duration_seconds>,<amplifier>#<clear previous effects AS boolean>
	
	private boolean isActive = true;
	
	private ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
	
	public PotionEffectAction(String npc, String data){
		String[] effects = data.split("#");
		for(String potion : effects){
			String[] info = potion.split(",");
			if(info.length != 3){
				QuestX.logError("Invalid potion effect specified for NPC '" + npc + "' in custom_trigger file.");
				this.isActive = false;
				return;
			} else {
				PotionEffectType type;
				if((type = PotionEffectType.getByName(info[0])) == null){
					try{
						int i = Integer.parseInt(info[0]);
						type = PotionEffectType.getById(i);
					} catch (NumberFormatException e){
						QuestX.logError("Invalid potion effect specified for NPC '" + npc + "' in custom_trigger file.");
						this.isActive = false;
						return;
					}
				}
				
				if(type == null){
					QuestX.logError("Invalid potion effect specified for NPC '" + npc + "' in custom_trigger file.");
					this.isActive = false;
					return;
				} else {
					int duration, amplitude;
					try{
						duration = Integer.parseInt(info[1]);
						amplitude = Integer.parseInt(info[2]);
					} catch (NumberFormatException e){
						QuestX.logError("Invalid potion effect duration/amplitude specified for NPC '" + npc + "' in custom_trigger file.");
						this.isActive = false;
						return;
					}
					PotionEffect eff = new PotionEffect(type, (duration * 20), amplitude);
					this.effects.add(eff);
				}
			}
		}
	}

	@Override
	public void implement(Player p) {
		for(PotionEffect ef : this.effects){
			//remove conflicting effects
			p.addPotionEffect(ef, true);
		}
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
