package couk.adamki11s.threads;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import couk.adamki11s.ai.MovementController;
import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.questx.QuestX;

public class ThreadController {
	
	BukkitTask asyncID, syncID;
	
	boolean asyncToggle, syncToggle;
	
	final NPCHandler handle;
	
	public ThreadController(NPCHandler handle){
		this.handle = handle;
	}
	
	public void initiateAsyncThread(long tickRate){
		asyncToggle = true;
		asyncID = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(QuestX.p, new AsyncThread(this.handle), 0L, tickRate);
	}
	
	public void initiateSyncronousThread(long tickRate){
		syncToggle = true;
		syncID = Bukkit.getServer().getScheduler().runTaskTimer(QuestX.p, new SyncThread(this.handle), 0L, tickRate);
	}
	
	public void terminateAsyncThread(){
		this.asyncID.cancel();
	}
	
	public void terminateSyncronousThread(){
		this.syncID.cancel();
	}

}
