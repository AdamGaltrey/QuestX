package com.adamki11s.dialogue.dynamic;

public enum DynVar {
	
	PLAYER_NAME("%playername%"),
	ONLINE_PLAYERS("%playercount%"),
	SERVER_NAME("%servername%"),
	MOTD("%motd%");
	
	final String var;
	
	DynVar(String s){
		this. var = s;
	}
	
	public String getDynVar(){
		return this.var;
	}

}
