package com.adamki11s.threads;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;

import com.adamki11s.ai.AttackController;
import com.adamki11s.ai.DespawnController;
import com.adamki11s.ai.GotoLocationThreadController;
import com.adamki11s.ai.HealthController;
import com.adamki11s.ai.MovementController;
import com.adamki11s.ai.RespawnController;
import com.adamki11s.io.DatabaseConfigData;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.npcs.population.PopulationDensityThread;
import com.adamki11s.pathing.decision.DecisionController;
import com.adamki11s.questx.QuestX;

public class AsyncThread implements Runnable {

	final NPCHandler handle;
	final MovementController mControl;
	final RespawnController rControl;
	final AttackController aControl;
	final PopulationDensityThread pdThread;
	final DespawnController dControl;
	final HealthController hControl;
	final DecisionController decisionControl;
	final GotoLocationThreadController glThread = new GotoLocationThreadController();
	final int tickRate;

	private volatile boolean running = true;

	private static AtomicInteger playerCount = new AtomicInteger(Bukkit.getServer().getOnlinePlayers().length);

	private static boolean playersOnline = true;

	public static void playerJoined() {
		if (playerCount.get() == 0) {
			// first player on the server ?
			if (!playersOnline) {
				//set boolean to true
				playersOnline ^= true;
			}
		}
		playerCount.incrementAndGet();
	}

	public static void playerLeft() {
		playerCount.decrementAndGet();
	}

	AsyncThread(NPCHandler handle, int tickRate) {
		this.handle = handle;
		mControl = new MovementController(handle);
		rControl = new RespawnController(handle);
		aControl = new AttackController(handle);
		pdThread = new PopulationDensityThread();
		dControl = new DespawnController(handle);
		hControl = new HealthController(handle);
		decisionControl = new DecisionController(handle);
		this.tickRate = tickRate;
		this.running = true;
	}

	public void onShutdown() {
		this.running = false;
		this.pdThread.terminateSQL();
	}

	int secondTickOver = 0, denstiyCalculationTickOver = 0, twoSecondTickOver = 0, fifteenSecTickOver = 0;

	@Override
	public void run() {
		if (running) {

			if (playerCount.get() != 0) {

				secondTickOver += tickRate;
				twoSecondTickOver += tickRate;
				fifteenSecTickOver += tickRate;
				denstiyCalculationTickOver += tickRate;
				// if(denstiyCalculationTickOver > (20 * 60 * 5)){ 5minutes
				if (denstiyCalculationTickOver >= (20 * 60 * DatabaseConfigData.getUpdateMinutes())) { // 60
																										// secs
																										// *
																										// x
																										// minutes
																										// between
																										// updates
					this.denstiyCalculationTickOver = 0;
					this.pdThread.run();
				}
				// run every second
				if (secondTickOver >= 20) {
					this.mControl.run();
					this.rControl.run(20);
					this.dControl.run(20);
					secondTickOver = 0;
				}

				// run every 2 seconds
				if (twoSecondTickOver >= 40) {
					this.hControl.run();
					this.twoSecondTickOver = 0;
				}

				// 30 secs = 600 ticks
				if (fifteenSecTickOver >= 300) {
					fifteenSecTickOver = 0;
					this.decisionControl.run();
				}

				aControl.run();
				glThread.run(tickRate);

			} else {
				
				// 0 players online
				if (playersOnline) {
					// invert boolean
					playersOnline ^= true;
					// run shutdown (clears memory)
					
					for(SimpleNPC npc : handle.getNPCs()){
						npc.purgeCachedData();
					}
					
					this.decisionControl.serverNoPlayersAction();
					this.aControl.serverNoPlayersAction();
				}
			}

		}
	}

}
