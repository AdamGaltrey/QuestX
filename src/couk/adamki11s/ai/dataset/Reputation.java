package couk.adamki11s.ai.dataset;

import java.io.Serializable;
import java.util.HashMap;

public class Reputation implements Serializable {

	private static final long serialVersionUID = -554972234451350229L;
	
	static HashMap<String, Reputation> playerRep = new HashMap<String, Reputation>();
	
	public static Reputation getPlayerReputation(String playerName){
		return playerRep.get(playerName);
	}
	
	final String playerName;
	
	protected RepLevel repLevel = RepLevel.NEUTRAL;
	
	protected int rep = 0;
	
	protected final int repSpread = 1000;
	
	public Reputation(String playerName){
		this.playerName = playerName;
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
	
	void sanityCheck(){
		if(this.rep < -repSpread){
			this.rep = -repSpread;
		} else if(this.rep > repSpread){
			this.rep = repSpread;
		}
	}
	
	//Max rep = 1000, Min rep = -1000
	
	

}
