package couk.adamki11s.events;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import couk.adamki11s.dialogue.Conversation;
import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.npcs.SimpleNPC;

public class ConversationRegister implements Listener {

	final NPCHandler handle;

	public static HashSet<Conversation> playersConversing = new HashSet<Conversation>();

	public ConversationRegister(Plugin p, NPCHandler handle) {
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		this.handle = handle;
	}

	public static boolean isPlayerConsversing(String pName) {
		for (Conversation c : playersConversing) {
			if (c != null) {
				if (c.getConvoData().getPlayer().getName().equalsIgnoreCase(pName)) {
					return c.isConversing();
				}
			}
		}
		return false;
	}

	public static boolean isPlayerWithinTalkingDistance(Player p) {
		for (Conversation c : playersConversing) {
			if (c != null) {
				if (c.getConvoData().getPlayer().getName().equalsIgnoreCase(p.getName())) {
					Location npcLoc = c.getConvoData().getSimpleNpc().getHumanNPC().getBukkitEntity().getLocation();
					Location pl = p.getLocation();
					c.getConvoData().getSimpleNpc().getHumanNPC().lookAtPoint(new Location(pl.getWorld(), pl.getX(), pl.getY() + 1, pl.getZ()));
					return (Math.abs(npcLoc.distance(pl)) < 5);
				}
			}
		}
		return false;
	}

	public static void endPlayerNPCConversation(Player p) {
		for (Conversation c : playersConversing) {
			if (c != null) {
				if (c.getConvoData().getPlayer().getName().equalsIgnoreCase(p.getName())) {
					p.sendMessage(ChatColor.AQUA + "[QuestX] " + ChatColor.RED + " Conversation ended.");
					c.endConversation();
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(final AsyncPlayerChatEvent evt) {
		for (Conversation c : playersConversing) {
			if (c != null) {
				if (c.getConvoData().getPlayer().getName().equalsIgnoreCase(evt.getPlayer().getName())) {
					String s = evt.getMessage();
					if (s.equalsIgnoreCase("exit") || s.equalsIgnoreCase("end") || s.equalsIgnoreCase("stop") || s.equalsIgnoreCase("quit") || s.equalsIgnoreCase("close")) {
						evt.getPlayer().sendMessage(ChatColor.AQUA + "[QuestX] " + ChatColor.RED + " Conversation ended.");
						c.endConversation();
						evt.setCancelled(true);
						return;
					}
					c.respond(evt.getMessage());
					evt.setCancelled(true);
				}
			}
		}
	}

}
