package com.adamki11s.npcs.triggers.action;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

import com.adamki11s.questx.QuestX;

public class PlayRecordAction implements Action {

	private boolean isActive = true;

	private int recordID;

	// PLAY_RECORD:<record block id>,<range>
	public PlayRecordAction(String npc, String data) {
		try {
			recordID = Integer.parseInt(data);
		} catch (NumberFormatException e) {
			this.isActive = false;
			QuestX.logError("Error parsing record id/range in custom_trigger file for NPC '" + npc + ". Setting disabled.");
			return;
		}
		
		if(!this.isTrackValid()){
			this.isActive = false;
			QuestX.logError("Invalid record block id specified for NPC '" + npc + ". Setting disabled.");
			return;
		}
	}

	@Override
	public void implement(Player p) {
		p.playEffect(p.getLocation(), Effect.RECORD_PLAY, recordID);
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}
	
	/*
	 * 2256 Music Disk (13) 2:58
	 * 2257 Music Disk (Cat) 3:05
	 * 2258 Music Disk (Blocks) 5:45
	 * 2259 Music Disk (Chirp) 3:05
	 * 2260 Music Disk (Far) 2:54
	 * 2261 Music Disk (Mall) 3:17
	 * 2262 Music Disk (Mellohi) 1:36
	 * 2263 Music Disk (Stal) 2:30
	 * 2264 Music Disk (Strad) 3:08
	 * 2265 Music Disk (Ward) 4:11
	 * 2266 Music Disk (11) 1:11
	 * 2267 Music Disk (Wait) 3:58
	 */

	private boolean isTrackValid() {
		for(int rec = 2256; rec <= 2267; rec++){
			if(rec == this.recordID){
				return true;
			}
		}
		return false;
	}

}
