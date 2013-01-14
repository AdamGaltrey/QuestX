package couk.adamki11s.threads;

import couk.adamki11s.ai.MovementController;
import couk.adamki11s.ai.RespawnController;
import couk.adamki11s.npcs.NPCHandler;

public class AsyncThread implements Runnable {
	
	final NPCHandler handle;
	final MovementController mControl;
	final RespawnController rControl;
	final int tickRate;
	
	AsyncThread(NPCHandler handle, int tickRate){
		this.handle = handle;
		mControl = new MovementController(handle);
		rControl = new RespawnController(handle);
		this.tickRate = tickRate;
	}
	
	@Override
	public void run() {
		this.mControl.run();
		this.rControl.run(tickRate);
	}

}
