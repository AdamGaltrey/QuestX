package com.adamki11s.display;

import java.lang.ref.SoftReference;

import org.bukkit.entity.Player;

import com.adamki11s.npcs.loading.FixedLoadingTable;
import com.adamki11s.questx.QuestX;

public class PresetPathsDisplay {
	
static SoftReference<Pages> pages = new SoftReference<Pages>(new Pages(FixedLoadingTable.getPresetPaths(), 8));
	
	public static void display(Player player, int page){
		if(pages.get() == null){
			String[] list = FixedLoadingTable.getPresetPaths();
			pages = new SoftReference<Pages>(new Pages(list, 8));
		}
		Pages p = pages.get();
		String[] send = p.getStringsToSend(page);
		QuestX.logChat(player, "Preset NPC Paths Page (" + page + "/" + p.getPages() + ") Displaying (" + send.length + "/" + p.getRawArrayLength() + ")");
		for(String s : send){
			QuestX.logChat(player, s);
		}
	}
	
	public static void updateSoftReference(){
		pages = new SoftReference<Pages>(new Pages(FixedLoadingTable.getPresetPaths(), 8));
	}

}
