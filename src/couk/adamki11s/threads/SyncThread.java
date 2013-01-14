package couk.adamki11s.threads;

import couk.adamki11s.ai.MovementController;
import couk.adamki11s.ai.RespawnController;
import couk.adamki11s.npcs.NPCHandler;

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
