package couk.adamki11s.io;

import java.io.File;

import org.bukkit.ChatColor;

public class CreateNPC {
	
	final String name;
	final String npc_root = FileLocator.npc_data_root;
	final ChatColor nameColour;
	final File folder;
	
	public static boolean isNameUnique(String name){
		File f = new File(FileLocator.npc_data_root + File.separator + name);
		return (!f.exists());
	}
	
	public CreateNPC(String name, ChatColor nameColour){
		this.name = name;
		this.nameColour = nameColour;
		this.folder = new File(npc_root + File.separator + name);
	}

}
