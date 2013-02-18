package com.adamki11s.commands.help;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.display.StaticStrings;
import com.adamki11s.questx.QuestX;

public class HelpDispatcher {
	
	private static final String[] hotspotCMDs = new String[]{"/questx hotspots list",
		"/questx hotspots list <page>",
		"/questx hotspots add <name> <range> <maxspawns>",
		"/questx hotspots edit <name> <range> <maxspawns>",
		"/questx hotspots delete <name>"
		},
		fixedSpawnCMDs = new String[]{"/questx fixedspawns list",
		"/questx fixedspawns list <page>",
		"/questx fixedspawns add <npcname>",
		"/questx fixedspawns edit <npcname>",
		"/questx fixedspawns delete <npcname>",
		"/questx fixedspawns deleteall"},
		taskCMDs = new String[]{"/questx task info",
		"/questx task cancel"},
		npcCMDs = new String[]{"/questx npc list",
		"/questx npc list <page>",
		"/questx npc delete <npcname>",
		"/questx npc find <npcname>",
		"/questx npc tele <npcname>"},
		questCMDs = new String[]{"/questx quest info",
		"/questx quest cancel",
		"/questx quest unpack <name>",
		"/questx quest setup <name>",
		"/questx quest next"},
		miscCMDs = new String[]{"/questx force-update"},
		pageList = new String[]{"[1/hotspots]"  +ChatColor.GREEN + " Hotspot Commands",
		"[2/fixedspawns]"  +ChatColor.GREEN + " Fixed Spawn Commands",
		"[3/tasks]"  +ChatColor.GREEN + " Task Commands",
		"[4/npcs]"  +ChatColor.GREEN + " NPC Commands",
		"[5/quests]"  +ChatColor.GREEN + " Quest Commands",
		"[6/misc]"  +ChatColor.GREEN + " Miscellaneous Commands"};
	
	public static void helpDispatcher(Player p, String[] args){
		//assuming arg[0] = help has been checked
		if(args.length == 1){
			//no specific page specified
			QuestX.logChat(p, ChatColor.GREEN + "" +  ChatColor.ITALIC + "Help " + ChatColor.RESET + StaticStrings.separator.substring(5));
			for(String s : pageList){
				QuestX.logChat(p, s);
			}
			return;
		} else if(args.length == 2){
			if(args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("hotspots")){
				QuestX.logChat(p, ChatColor.GREEN + "" +  ChatColor.ITALIC + "Hotspot Commands " + ChatColor.RESET + StaticStrings.separator.substring(17));
				for(String s : hotspotCMDs){
					QuestX.logChat(p, s);
				}
				return;
			} else if(args[1].equalsIgnoreCase("2") || args[1].equalsIgnoreCase("fixedspawns")){
				QuestX.logChat(p, ChatColor.GREEN + "" +  ChatColor.ITALIC + "Fixed Spawn Commands " + ChatColor.RESET + StaticStrings.separator.substring(17));
				for(String s : fixedSpawnCMDs){
					QuestX.logChat(p, s);
				}
				return;
			}else if(args[1].equalsIgnoreCase("3") || args[1].equalsIgnoreCase("tasks")){
				QuestX.logChat(p, ChatColor.GREEN + "" +  ChatColor.ITALIC + "Task Commands " + ChatColor.RESET + StaticStrings.separator.substring(14));
				for(String s : taskCMDs){
					QuestX.logChat(p, s);
				}
				return;
			}else if(args[1].equalsIgnoreCase("4") || args[1].equalsIgnoreCase("npcs")){
				QuestX.logChat(p, ChatColor.GREEN + "" +  ChatColor.ITALIC + "NPC Commands " + ChatColor.RESET + StaticStrings.separator.substring(13));
				for(String s : npcCMDs){
					QuestX.logChat(p, s);
				}
				return;
			}else if(args[1].equalsIgnoreCase("5") || args[1].equalsIgnoreCase("quests")){
				QuestX.logChat(p, ChatColor.GREEN + "" +  ChatColor.ITALIC + "Quest Commands " + ChatColor.RESET + StaticStrings.separator.substring(15));
				for(String s : questCMDs){
					QuestX.logChat(p, s);
				}
				return;
			}else if(args[1].equalsIgnoreCase("6") || args[1].equalsIgnoreCase("misc")){
				QuestX.logChat(p, ChatColor.GREEN + "" +  ChatColor.ITALIC + "Miscellaneous Commands " + ChatColor.RESET + StaticStrings.separator.substring(23));
				for(String s : miscCMDs){
					QuestX.logChat(p, s);
				}
				return;
			}
			//specific page specified
		}
		QuestX.logChatError(p, ChatColor.RED + "Invalid syntax. Syntax should be as follows.");
		QuestX.logChatError(p, ChatColor.RED + "/questx help " + ChatColor.RESET + "or " + ChatColor.RED + "/questx help <pagetag>");
		QuestX.logChatError(p, "EG : " + ChatColor.GREEN + "/questx help quests " + ChatColor.RESET + "or " + ChatColor.GREEN + "/questx help 5");
		
	}

}
