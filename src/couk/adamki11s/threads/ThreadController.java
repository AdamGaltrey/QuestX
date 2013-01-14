package couk.adamki11s.threads;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import couk.adamki11s.questx.QuestX;

public class ThreadController {
	
	BukkitTask asyncID, syncID;
	
	boolean asyncToggle, syncToggle;
	
	public void initiateAsyncThread(int tickRate){
		asyncToggle = true;
		asyncID = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(QuestX.p, new AsyncThread(), 0L, tickRate);
	}
	
	public void initiateSyncronousThread(int tickRate){
		syncToggle = true;
		syncID = Bukkit.getServer().getScheduler().runTaskTimer(QuestX.p, new AsyncThread(), 0L, tickRate);
	}
	
	public void terminateAsyncThread(){
		this.asyncID.cancel();
	}
	
	public void terminateSyncronousThread(){
		this.syncID.cancel();
	}

}
