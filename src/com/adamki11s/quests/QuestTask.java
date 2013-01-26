package com.adamki11s.quests;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.extras.inventory.ExtrasInventory;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.tasks.EntityKillTracker;
import com.adamki11s.npcs.tasks.NPCKillTracker;
import com.adamki11s.npcs.tasks.NPCTalkTracker;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class QuestTask {

	final QType type;
	final Object taskData;
	final String npcComplete, completeTaskText;

	// no node, they should be loaded and stored in order
	public QuestTask(QType type, Object taskData, String npcComplete, String completeTaskText) {
		this.type = type;
		this.taskData = taskData;
		this.npcComplete = npcComplete;
		this.completeTaskText = completeTaskText;
	}

	/*
	 * Fetch items -instanceof- ItemStack[] KILL_ENTITIES -instanceof-
	 * EntityKillTracker KILL_NPC -instanceof- NPCKillTracker TALK_NPC
	 * -instanceof- String (NPC_Name)
	 */
	public Object getData() {
		return this.taskData;
	}

	public QuestTask getClonedInstance() {
		return new QuestTask(type, taskData, npcComplete, completeTaskText);
	}

	// The NPC who the player needs to return too to complete the task.
	public String getNPCToCompleteName() {
		return this.npcComplete;
	}

	public boolean isTaskComplete(Player p) {
		if (this.isItemStacks()) {
			return (this.areRequiredItemsGathered(p));
		} else if (this.isKillEntities()) {
			return ((EntityKillTracker) this.taskData).areRequiredEntitiesKilled();
		} else if (this.isKillNPC()) {
			return ((NPCKillTracker) this.taskData).areRequiredNPCSKilled();
		} else {
			return ((NPCTalkTracker) this.taskData).isCompleted();
		}
	}

	boolean areRequiredItemsGathered(Player p) {
		ItemStack[] req = (ItemStack[]) this.taskData;
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

	public boolean isItemStacks() {
		return (this.taskData instanceof ItemStack[]);
	}

	public boolean isKillEntities() {
		return (this.taskData instanceof EntityKillTracker);
	}

	public boolean isKillNPC() {
		return (this.taskData instanceof NPCKillTracker);
	}

	public boolean isTalkNPC() {
		return (this.taskData instanceof NPCTalkTracker);
	}

	public String getCompleteTaskText() {
		return this.completeTaskText;
	}

	public void sendWhatIsLeftToDo(Player p) {
		if (this.isItemStacks()) {
			p.sendMessage(this.sendItemsToGather(p));
			return;
		} else if (this.isKillEntities()) {
			p.sendMessage(((EntityKillTracker) this.taskData).sendEntitiesToKill());
			return;
		} else if (this.isKillNPC()) {
			p.sendMessage(((NPCKillTracker) this.taskData).sendNPCSToKill());
			return;
		} else if (this.isTalkNPC()) {
			p.sendMessage(((NPCTalkTracker) this.taskData).sendWhoToTalkTo());
			return;
		}
		p.sendMessage("Looks like there was an error? You have no task.");
	}
	
	public void removeItems(Player p){
		ExtrasInventory ei = new ExtrasInventory();

		if (this.isItemStacks()) {
			for (ItemStack is : ((ItemStack[])this.taskData)) {
				ei.removeFromInventory(p, is.getTypeId(), is.getAmount());
			}
		}
	}

	String sendItemsToGather(Player p) {
		ItemStack[] req = (ItemStack[]) this.taskData;
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

}
