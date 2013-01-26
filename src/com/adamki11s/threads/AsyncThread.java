package com.adamki11s.threads;

import com.adamki11s.ai.AttackController;
import com.adamki11s.ai.MovementController;
import com.adamki11s.ai.RespawnController;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.population.PopulationDensityThread;


public class AsyncThread implements Runnable {

	final NPCHandler handle;
	final MovementController mControl;
	final RespawnController rControl;
	final AttackController aControl;
	final PopulationDensityThread pdThread;
	final int tickRate;

	AsyncThread(NPCHandler handle, int tickRate) {
		this.handle = handle;
		mControl = new MovementController(handle);
		rControl = new RespawnController(handle);
		aControl = new AttackController(handle);
		pdThread = new PopulationDensityThread();
		this.tickRate = tickRate;
	}
	
	public void onShutdown(){
		this.pdThread.terminateSQL();
	}

	int tickOver = 0, denstiyCalculationTickOver = 0;

	@Override
	public void run() {
		tickOver += tickRate;
		denstiyCalculationTickOver += tickRate;
		//if(denstiyCalculationTickOver > (20 * 60 * 5)){ 5minutes
		if(denstiyCalculationTickOver > (20 * 10)){ //10 secs
			this.denstiyCalculationTickOver = 0;
			this.pdThread.run();
		}
		if (tickOver % tickRate == 0) {
			this.mControl.run();
			this.rControl.run(tickRate);
			tickOver = 0;
		}
		aControl.run();
	}

}
