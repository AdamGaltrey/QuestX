package couk.adamki11s.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.topcat.npclib.entity.HumanNPC;
import couk.adamki11s.ai.RandomMovement;
import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.questx.QuestX;

public class QuestXCommands implements CommandExecutor {
	
	QuestX plugin;
	NPCHandler handle;
	
	public QuestXCommands(QuestX main){
		this.plugin = main;
		this.handle = main.getNPCHandler();
	}
	
	HumanNPC test;
	RandomMovement rm;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("questx")){
			if(!(sender instanceof Player)){
				System.out.println("QuestX Commands must be issued in-game.");
				return true;
			} else {
				Player p = (Player) sender;
				
				if(args.length == 1 && args[0].equalsIgnoreCase("spawn-npc")){
					test = (HumanNPC) handle.getNPCManager().spawnHumanNPC("Adamki11s (100/100)", p.getLocation());
					rm = new RandomMovement(test, test.getBukkitEntity().getLocation(), 200,500, 10);//min wait between moving = 10secs, max = 25 secs, moves max of 10 blocks from root
					Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(QuestX.p, new Runnable(){
						public void run(){
							rm.move();
						}
					}, 0, 20);
					
				}
			}
		}
		return true;
	}

}
