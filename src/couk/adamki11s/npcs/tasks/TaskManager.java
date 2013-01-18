package couk.adamki11s.npcs.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import couk.adamki11s.questx.QuestX;

public class TaskManager {

	final String pName;

	TaskLoader currentTask;

	public TaskManager(String playerName, TaskLoader task) {
		this.pName = playerName;
		this.currentTask = task;
		QuestX.logMSG("TaskManager instantitated");
	}

	public TaskLoader getTaskLoader() {
		return this.currentTask;
	}

	public boolean isTrackingEntityKills() {
		return this.getTaskLoader().isKillEntities();
	}
	
	public boolean doesNeedToFetchItems(){
		return this.getTaskLoader().isFetchItems();
	}

	public void trackEntityKill(EntityType e) {
		this.getTaskLoader().getEKT().trackKill(e);
	}

	public String whatIsLeftToDo() {
		// TODO
		return null;
	}

	public boolean isTaskCompleted() {
		boolean kills = true, items = true;
		if (this.isTrackingEntityKills()) {
			kills = this.areEntityKillsCompleted();
		}
		if(this.doesNeedToFetchItems()){
			items = this.areRequiredItemsGathered();
		}
		return (kills && items);
	}

	boolean areEntityKillsCompleted() {
		return this.getTaskLoader().getEKT().areRequiredEntitiesKilled();
	}

	boolean areRequiredItemsGathered() {
		ItemStack[] req = this.getTaskLoader().getRequiredItems();
		Player p = Bukkit.getServer().getPlayer(pName);
		if (p != null) {
			ItemStack[] pContents = p.getInventory().getContents();
			for (ItemStack is : req) {
				int currentIsSum = 0;
				for (ItemStack contained : pContents) {
					if (contained != null) {
						if (contained.getTypeId() == is.getTypeId() && contained.getData().getData() == is.getData().getData()) {
							currentIsSum += contained.getAmount();
						}
					}
				}
				if (currentIsSum < is.getAmount()) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public String getPlayerName() {
		return this.pName;
	}

	public String getIncompleteTaskSpeech() {
		return this.getTaskLoader().getIncompleteTaskSpeech();
	}
	
	public void sendWhatIsLeftToDo(Player p){
		boolean kills = true, items = true;
		if (this.isTrackingEntityKills()) {
			kills = this.areEntityKillsCompleted();
		}
		if(this.doesNeedToFetchItems()){
			items = this.areRequiredItemsGathered();
		}
		if(!kills){
			p.sendMessage(this.getTaskLoader().getEKT().sendEntitiesToKill());
		}
		if(!items){
			p.sendMessage(this.sendItemsToGather());
		}
	}
	
	String sendItemsToGather(){
			ItemStack[] req = this.getTaskLoader().getRequiredItems();
			Player p = Bukkit.getServer().getPlayer(pName);
			StringBuilder buff = new StringBuilder();
			buff.append(ChatColor.RED).append("Collect : ").append(ChatColor.RESET);
			if (p != null) {
				ItemStack[] pContents = p.getInventory().getContents();
				for (ItemStack is : req) {
					int currentIsSum = 0;
					for (ItemStack contained : pContents) {
						if (contained != null) {
							if (contained.getTypeId() == is.getTypeId() && contained.getData().getData() == is.getData().getData()) {
								currentIsSum += contained.getAmount();
							}
						}
					}
					if (currentIsSum < is.getAmount()) {
						buff.append(is.getAmount() - currentIsSum).append(" ").append(is.getType().toString()).append(", ");
					}
				}
				return buff.toString();
			} else {
				return "";
			}
	}

	public String getCompleteTaskSpeech() {
		return this.getTaskLoader().getCompleteTaskSpeech();
	}

}
