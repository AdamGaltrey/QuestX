package com.adamki11s.reputation;

import java.io.Serializable;
import java.util.HashMap;

public class Reputation {
	
	final String playerName;
	
	protected RepLevel repLevel;
	
	protected int rep = 0;
	
	protected final int repSpread = 1000;
	
	public Reputation(String playerName){
		this.playerName = playerName;
		this.rep = 0;
		this.repLevel = RepLevel.getRepLevel(this.rep);
	}
	
	public Reputation(String playerName, int currentRep){
		this.playerName = playerName;
		this.rep = currentRep;
		this.repLevel = RepLevel.getRepLevel(this.rep);
	}
	
	public void addRep(int rep){
		this.rep += rep;
		this.sanityCheck();
		this.repLevel = RepLevel.getRepLevel(this.rep);
	}
	
	public void subtractRep(int rep){
		this.rep -= rep;
		this.sanityCheck();
		this.repLevel = RepLevel.getRepLevel(this.rep);
	}
	
	public int getRep(){
		return this.rep;
	}
	
	public void setRep(int rep){
		this.rep = rep;
	}
	
	void sanityCheck(){
		if(this.rep < -repSpread){
			this.rep = -repSpread;
		} else if(this.rep > repSpread){
			this.rep = repSpread;
		}
	}
	
	//Max rep = 1000, Min rep = -1000
	
	

}
