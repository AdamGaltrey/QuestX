package com.adamki11s.dialogue.dynamic;

import org.bukkit.Bukkit;

public class DynamicStrings {
	
	public static final String getDynamicReplacement(String in, String reciever){
		String rep1 = in.replaceAll(DynVar.PLAYER_NAME.getDynVar(), reciever);
		String rep2 = rep1.replaceAll(DynVar.ONLINE_PLAYERS.getDynVar(), (Bukkit.getServer().getOnlinePlayers().length + ""));
		String rep3 = rep2.replaceAll(DynVar.MOTD.getDynVar(), Bukkit.getServer().getMotd());
		String rep4 = rep3.replaceAll(DynVar.SERVER_NAME.getDynVar(), Bukkit.getServerName());
		return rep4;
	}

}
