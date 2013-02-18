package com.adamki11s.commands.help;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.adamki11s.questx.QuestX;

public class HelpBook {

	/*
	 * ItemStack i = new ItemStack(Material.WRITTEN_BOOK, 1); BookMeta bm =
	 * (BookMeta)i.getItemMeta(); bm.setAuthor("Fireblast709");
	 * bm.setTitle("Cooking pork in 50 ways"); ArrayList<String> pages = new
	 * ArrayList<String>(); pages.add("With vire"); pages.add("In a furnace");
	 * bm.setPages(pages); bm.setPage(0, "With fire"); i.setItemMeta(bm);
	 */

	public static void giveHelpBook(Player p) {
		for (ItemStack i : p.getInventory().getContents()) {
			if (i != null) {
				if (i.getType() == Material.ENCHANTED_BOOK || i.getType() == Material.WRITTEN_BOOK) {
					ItemMeta meta = i.getItemMeta();
					if (meta.hasLore()) {
						List<String> lore = meta.getLore();
						for (String s : lore) {
							if (s.equalsIgnoreCase("QuestX Help Book")) {
								// QuestX.logChat(p, ChatColor.RED +
								// "You already have a help book in your inventory!");
								return;
							}
						}
					}
				}
			}
		}

		ArrayList<String> lore = new ArrayList<String>();
		lore.add("QuestX Help Book");

		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta meta = (BookMeta) book.getItemMeta();

		meta.setLore(lore);

		meta.setAuthor("Adamki11s");
		meta.setTitle("QuestX Help");

		ArrayList<String> pages = new ArrayList<String>();

		pages.add("QuestX Help Book");
		pages.add("NPCs");
		pages.add("Quests");
		pages.add("Tasks");
		pages.add("Configuration");

		meta.setPages(pages);

		// 256 characters per page.
		// No page may be longer than 13 lines and each line can have up to 19
		// characters. "

		meta.setPage(1, getTitlePage());
		meta.setPage(2, getNPCPage1());
		meta.setPage(3, getQuestPage1());
		meta.setPage(4, getTaskPage1());
		meta.setPage(5, getConfigPage1());

		book.setItemMeta(meta);

		p.getInventory().addItem(book);

		QuestX.logChat(p, ChatColor.YELLOW + "A help book has been added to your inventory!");
	}

	private static final String separator = "-------------------";

	private static String getTitlePage() {
		StringBuilder b = new StringBuilder();
		b.append("QuestX Help Book\n");
		b.append(separator);
		b.append("Pg 1 : NPCs\n");
		b.append("Pg 2 : Quests\n");
		b.append("Pg 3 : Tasks\n");
		b.append("Pg 4 : Configuration\n");
		b.append(separator);
		b.append("QuestX was developed by Adamki11s. If you notice any bugs or problems please report them at http://dev.bukkit.org/server-mods/questx");
		return b.toString();
	}

	private static String getNPCPage1() {
		StringBuilder b = new StringBuilder();
		b.append("NPCs\n");
		b.append(separator);
		b.append("NPCs can be found all over the world.\nRight click on an NPC to speak with it. To end the conversation early type 'exit', 'end', 'stop', 'quit' or 'close'.\n");
		b.append("Walking away from an NPC will end the conversation automatically.\n");
		return b.toString();
	}

	private static String getQuestPage1() {
		StringBuilder b = new StringBuilder();
		b.append("Quests\n");
		b.append(separator);
		b.append("You can get a quest by speaking to an NPC. Your progression in Quests is saved so you can come back and complete Quests which were started earlier.\n");
		b.append("You can only undertake 1 Quest at a time.");
		return b.toString();
	}

	private static String getTaskPage1() {
		StringBuilder b = new StringBuilder();
		b.append("Tasks\n");
		b.append(separator);
		b.append("Tasks are shorter versions of Quests. They have a more limited set of tasks and your progress is not saved. You can only undertake 1 Task at a time.");
		return b.toString();
	}

	private static String getConfigPage1() {
		StringBuilder b = new StringBuilder();
		b.append("Configuration\n");
		b.append(separator);
		b.append("In the folder /plugins/QuestX/Configuration there are a number of files which can be modified to change the way certain features of QuestX work.");
		return b.toString();
	}
}
