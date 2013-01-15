package couk.adamki11s.io;

import java.io.File;

import org.bukkit.ChatColor;

public class CreateNPC {

	final String npc_root = FileLocator.npc_data_root;
	final File folder;

	public static boolean isNameUnique(String name) {
		File f = new File(FileLocator.npc_data_root + File.separator + name);
		return (!f.exists());
	}

	final ChatColor nameColour;
	final String name;

	public CreateNPC(String name, ChatColor nameColour) {
		this.name = name;
		this.nameColour = nameColour;
		this.folder = new File(npc_root + File.separator + name);
	}

	boolean moveable, attackable, aggressive;
	int minPauseTicks, maxPauseTicks, maxVariation, respawnTicks, maxHealth, damageMod, retalliationMultiplier;

	public void setProperties() {

	}

}
