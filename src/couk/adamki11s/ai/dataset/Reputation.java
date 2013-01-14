package couk.adamki11s.ai.dataset;

import java.io.Serializable;
import java.util.HashMap;

public class Reputation implements Serializable {

	private static final long serialVersionUID = -554972234451350229L;
	
	public static HashMap<String, Reputation> playerRep = new HashMap<String, Reputation>();
	
	final String playerName;
	
	protected int rep = 0;
	
	protected final int repSpread;
	
	public Reputation(String playerName){
		this.playerName = playerName;
	}
	
	public void addRep(int rep){
		this.rep += rep;
		this.sanityCheck();
	}
	
	public void subtractRep(int rep){
		this.rep -= rep;
		this.sanityCheck();
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
