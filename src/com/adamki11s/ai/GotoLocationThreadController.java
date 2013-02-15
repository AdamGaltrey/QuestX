package com.adamki11s.ai;

import com.adamki11s.quests.locations.GotoLocationController;

public class GotoLocationThreadController {
	
	private int tickOver = 0;
	
	public void run(int tickRate){
		tickOver += tickRate;
		if(tickOver > 40){
			//every 2 seconds
			tickOver = 0;
			GotoLocationController.run();
		}
	}

}
