package com.adamki11s.threads;

import com.adamki11s.ai.MovementController;
import com.adamki11s.ai.RespawnController;
import com.adamki11s.npcs.NPCHandler;


public class SyncThread implements Runnable {

	final NPCHandler handle;
	final int tickRate;

	SyncThread(NPCHandler handle, int tickRate) {
		this.handle = handle;
		this.tickRate = tickRate;
	}
	

	@Override
	public void run() {

	}

}
