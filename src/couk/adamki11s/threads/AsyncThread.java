package couk.adamki11s.threads;

import couk.adamki11s.ai.AttackController;
import couk.adamki11s.ai.MovementController;
import couk.adamki11s.ai.RespawnController;
import couk.adamki11s.npcs.NPCHandler;

public class AsyncThread implements Runnable {

	final NPCHandler handle;
	final MovementController mControl;
	final RespawnController rControl;
	final AttackController aControl;
	final int tickRate;

	AsyncThread(NPCHandler handle, int tickRate) {
		this.handle = handle;
		mControl = new MovementController(handle);
		rControl = new RespawnController(handle);
		aControl = new AttackController(handle);
		this.tickRate = tickRate;
	}

	int tickOver = 0;

	@Override
	public void run() {
		tickOver += tickRate;
		if (tickOver % tickRate == 0) {
			this.mControl.run();
			this.rControl.run(tickRate);
			tickOver = 0;
		}
		aControl.run();
	}

}
