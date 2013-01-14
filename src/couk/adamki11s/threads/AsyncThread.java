package couk.adamki11s.threads;

import couk.adamki11s.ai.MovementController;
import couk.adamki11s.npcs.NPCHandler;

public class AsyncThread implements Runnable {
	
	final NPCHandler handle;
	final MovementController mControl;
	
	AsyncThread(NPCHandler handle){
		this.handle = handle;
		mControl = new MovementController(handle);
	}
	
	@Override
	public void run() {
		this.mControl.run();
	}

}
