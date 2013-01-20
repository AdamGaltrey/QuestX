package com.adamki11s.threads;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.adamki11s.ai.MovementController;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.questx.QuestX;

public class ThreadController {

	BukkitTask asyncID, syncID;

	boolean asyncToggle, syncToggle;

	SyncThread st;
	AsyncThread ast;

	final NPCHandler handle;

	public ThreadController(NPCHandler handle) {
		this.handle = handle;
	}

	public void initiateAsyncThread(long tickRate) {
		asyncToggle = true;
		ast = new AsyncThread(this.handle, (int) tickRate);
		asyncID = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(QuestX.p, ast, 0L, tickRate);
	}

	public void initiateSyncronousThread(long tickRate) {
		syncToggle = true;
		st = new SyncThread(this.handle, (int) tickRate);
		syncID = Bukkit.getServer().getScheduler().runTaskTimer(QuestX.p, st, 0L, tickRate);
	}

	public void terminateAsyncThread() {
		if (this.asyncToggle) {
			this.ast.onShutdown();
			this.asyncID.cancel();
		}
	}

	public void terminateSyncronousThread() {
		if (this.syncToggle) {
			this.st.onShutdown();
			this.syncID.cancel();
		}
	}

}
