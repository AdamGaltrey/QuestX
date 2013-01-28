package com.adamki11s.reputation;

import org.bukkit.ChatColor;

public enum RepLevel {
	
	GUARDIAN("Guardian", 800, ChatColor.DARK_RED),
	LAWMAN("Lawman", 600, ChatColor.RED),
	SHERIFF("Sheriff", 400, ChatColor.GOLD),
	OFFICER("Officer", 200, ChatColor.DARK_GRAY),
	NEUTRAL("Neutral", 0, ChatColor.RESET),
	THUG("Thug", -200, ChatColor.AQUA),
	CRIMINAL("Criminal", -400, ChatColor.DARK_AQUA),
	HITMAN("Hitman", -600, ChatColor.DARK_GREEN),
	VILLAIN("Villain", -800, ChatColor.GREEN);
	
	
	final String tag;
	final int minRep;
	final ChatColor colour;
	
	RepLevel(String tag, int minRep, ChatColor colour){
		this.tag = tag;
		this.minRep = minRep;
		this.colour = colour;
	}
	
	public String getRep(){
		return tag;
	}
	
	public ChatColor getColour(){
		return this.colour;
	}
	
	public int getMinRep(){
		return this.minRep;
	}
	
	public static RepLevel getRepLevel(int rep){
		if(THUG.getMinRep() < rep && rep < OFFICER.getMinRep()){
			return NEUTRAL;
		}
		if(rep < 0){
			if(rep <= VILLAIN.getMinRep()){
				return VILLAIN;
			} else if (rep <= HITMAN.getMinRep()){
				return HITMAN;
			} else if (rep <= CRIMINAL.getMinRep()){
				return CRIMINAL;
			} else if (rep <= THUG.getMinRep()){
				return THUG;
			}
		} else if(rep > 0){
			if(rep >= GUARDIAN.getMinRep()){
				return GUARDIAN;
			} else if(rep >= LAWMAN.getMinRep()){
				return LAWMAN;
			} else if(rep >= SHERIFF.getMinRep()){
				return SHERIFF;
			} else if(rep >= OFFICER.getMinRep()){
				return OFFICER;
			}
		}
		return null;
	}

}
