package com.adamki11s.npcs.triggers.action;

import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.adamki11s.questx.QuestX;

public class PlayRecordAction implements Action {

	private boolean isActive = true;

	private int recordID, range;

	private long lastPlay = -1, playLengthSeconds = -1;

	// PLAY_RECORD:<record block id>,<range>
	public PlayRecordAction(String npc, String data) {
		String[] split = data.split("#");
		try {
			recordID = Integer.parseInt(split[0]);
			range = Integer.parseInt(split[1]);
			if(range <= 0){
				this.isActive = false;
				QuestX.logError("Block record play range must be at least 1 '" + npc + ". Setting disabled.");
				return;
			}
		} catch (NumberFormatException e) {
			this.isActive = false;
			QuestX.logError("Error parsing record id/range in custom_trigger file for NPC '" + npc + ". Setting disabled.");
			return;
		}
		
		if((this.playLengthSeconds = this.getTrackLengthSeconds()) == -1){
			this.isActive = false;
			QuestX.logError("Invalid record block id specified for NPC '" + npc + ". Setting disabled.");
			return;
		}
	}

	@Override
	public void implement(Player p) {
		if(lastPlay != -1){
			long milliDiff = System.currentTimeMillis() - this.lastPlay;
			long secondsDiff = (milliDiff / 1000);
			if(secondsDiff < this.playLengthSeconds){
				//if track has not finished playing cancel
				return;
			}
		}
		World w = p.getWorld();
		w.playEffect(p.getLocation(), Effect.RECORD_PLAY, recordID, range);
		this.lastPlay = System.currentTimeMillis();
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

	private long getTrackLengthSeconds() {
		switch (recordID) {
		case 2256:
			return 178;
		case 2257:
			return 185;
		case 2258:
			return 345;
		case 2259:
			return 185;
		case 2260:
			return 174;
		case 2261:
			return 197;
		case 2262:
			return 96;
		case 2263:
			return 150;
		case 2264:
			return 188;
		case 2265:
			return 251;
		case 2266:
			return 71;
		case 2267:
			return 238;
		}
		return -1;
	}

}
