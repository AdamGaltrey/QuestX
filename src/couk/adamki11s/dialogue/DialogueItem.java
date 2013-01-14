package couk.adamki11s.dialogue;

import couk.adamki11s.ai.dataset.GenericRepLevel;
import couk.adamki11s.ai.dataset.Reputation;

public class DialogueItem {
	
	final String s;
	final GenericRepLevel requriedRep;
	
	public DialogueItem(String s, GenericRepLevel rep){
		this.s = s;
		this.requriedRep = rep;
	}
	
	public String getString(){
		return this.s;
	}
	
	public boolean doesPlayerHaveRequiredRepLevel(String pName){
		Reputation r = Reputation.getPlayerReputation(pName);
		int repPoints = r.getRep();
		GenericRepLevel repLevel = GenericRepLevel.getGenericReputation(repPoints);
		return repLevel.equals(this.requriedRep);
	}
	
	//TRIGGERS

}
