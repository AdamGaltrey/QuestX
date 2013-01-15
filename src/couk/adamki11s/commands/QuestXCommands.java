package couk.adamki11s.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.topcat.npclib.entity.HumanNPC;
import couk.adamki11s.ai.RandomMovement;
import couk.adamki11s.ai.dataset.Reputation;
import couk.adamki11s.data.ItemStackDrop;
import couk.adamki11s.data.ItemStackProbability;
import couk.adamki11s.dialogue.Conversation;
import couk.adamki11s.npcs.BanditNPC;
import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.npcs.SimpleNPC;
import couk.adamki11s.npcs.UniqueNameRegister;
import couk.adamki11s.questx.QuestX;

public class QuestXCommands implements CommandExecutor {

	QuestX plugin;
	NPCHandler handle;

	public QuestXCommands(QuestX main) {
		this.plugin = main;
		this.handle = main.getNPCHandler();
	}

	HumanNPC test;
	RandomMovement rm;
	
	Reputation r = new Reputation("Adamki11s", 100);

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("questx")) {
			if (!(sender instanceof Player)) {
				System.out.println("QuestX Commands must be issued in-game.");
				return true;
			} else {
				Player p = (Player) sender;

				if (args.length == 2 && args[0].equalsIgnoreCase("stressspawn")) {
					int max = Integer.parseInt(args[1]);
					for (int i = 0; i < max; i++) { //1/10 chance of dropping
						SimpleNPC snpc = new SimpleNPC(this.handle, ("a" + i), ChatColor.BLUE, p.getLocation(), true, true, false, 60, 200, 20, 100, 200, new ItemStackDrop(new ItemStackProbability[]{new ItemStackProbability(new ItemStack(Material.GOLD_AXE, 1), 6000)}));
						snpc.spawnNPC();
						// p.sendMessage("NPC Spawned!");
					}
					return true;
				}

				if (args.length == 2 && args[0].equalsIgnoreCase("spawn")) {
					String npcName = args[1];
					if (!UniqueNameRegister.isNameUnique(npcName)) {
						p.sendMessage(ChatColor.RED + "Name is not unique!");
						return true;
					} else {
						SimpleNPC snpc = new SimpleNPC(this.handle, npcName, ChatColor.BLUE, p.getLocation(), true, true, false, 60, 200, 10, 100, 200, new ItemStackDrop(new ItemStackProbability[]{new ItemStackProbability(new ItemStack(Material.GOLD_AXE, 1), 6000)}));
						snpc.spawnNPC();
					
						p.sendMessage("NPC Spawned!");
						return true;
					}

				}
			}
		}
		return true;
	}

}
