package couk.adamki11s.threads;

import couk.adamki11s.npcs.NPCHandler;

public class SyncThread implements Runnable {

	final NPCHandler handle;

	SyncThread(NPCHandler handle) {
		this.handle = handle;
	}

	@Override
	public void run() {

	}

}
