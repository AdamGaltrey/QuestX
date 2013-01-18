package couk.adamki11s.npcs.tasks;

import java.io.File;

import org.bukkit.inventory.ItemStack;

import couk.adamki11s.io.SyncConfiguration;

public class TaskLoader {

	private final File taskFile;

	final String npcName;
	String taskName;
	ItemStack[] retrieveItems, rewardItems;
	int rewardExp, rewardRep;
	EntityKillTracker ekt;
	boolean fetchItems, killEntities, awardItems;

	public TaskLoader(File taskFile, String npcName) {
		this.taskFile = taskFile;
		this.npcName = npcName;
	}

	public void load() {
		SyncConfiguration config = new SyncConfiguration(this.taskFile);
		config.read();
		this.taskName = config.getString("TASK_NAME");

		if (!config.getString("FETCH_ITEMS").trim().equalsIgnoreCase("0")) {
			this.retrieveItems = ISAParser.parseISA(config.getString("FETCH_ITEMS"));
			this.fetchItems = true;
		} else {
			this.fetchItems = false;
		}

		if (!config.getString("REWARD_ITEMS").trim().equalsIgnoreCase("0")) {
			this.rewardItems = ISAParser.parseISA(config.getString("REWARD_ITEMS"));
			this.awardItems = true;
		} else {
			this.awardItems = false;
		}
		
		if (!config.getString("KILL_ENTITIES").trim().equalsIgnoreCase("0")) {
			this.ekt = new EntityKillTracker(config.getString("KILL_ENTITIES"));
			this.killEntities = true;
		} else {
			this.killEntities = false;
		}

		this.rewardExp = config.getInt("REWARD_EXP");
		this.rewardRep = config.getInt("REWARD_REP");
		
		config.write();
	}

	public String getNpcName() {
		return npcName;
	}

	public String getTaskName() {
		return taskName;
	}

	public boolean isFetchItems() {
		return fetchItems;
	}

	public boolean isKillEntities() {
		return killEntities;
	}

	public boolean isAwardItems() {
		return awardItems;
	}
	
	public EntityKillTracker getEKT(){
		return this.ekt;
	}
	
	public ItemStack[] getRequiredItems(){
		return this.retrieveItems;
	}
	
	

}
