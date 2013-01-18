package couk.adamki11s.npcs.tasks;

import org.bukkit.Bukkit;
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

	public void trackEntityKill(EntityType e) {
		this.getTaskLoader().getEKT().trackKill(e);
	}
	
	public String whatIsLeftToDo(){
		//TODO
		return null;
	}
	
	public boolean isTaskComplete(){
		return (this.areEntityKillsCompleted() && this.areRequiredItemsGathered());
	}

	boolean areEntityKillsCompleted() {
		return this.getTaskLoader().getEKT().areKillsCompleted();
	}

	boolean areRequiredItemsGathered() {
		ItemStack[] req = this.getTaskLoader().getRequiredItems();
		Player p = Bukkit.getServer().getPlayer(pName);
		if (p != null) {
			ItemStack[] pContents = p.getInventory().getContents();
			for (ItemStack is : req) {
				int currentIsSum = 0;
				for (ItemStack contained : pContents) {
					if (contained.getTypeId() == is.getTypeId() && contained.getData().getData() == is.getData().getData()) {
						currentIsSum += contained.getAmount();
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

	public boolean isTaskCompleted() {
		// TODO
		return false;
	}

}
