package com.adamki11s.dialogue;

import java.util.ArrayList;
import java.util.List;

import com.adamki11s.dialogue.triggers.Trigger;
import com.adamki11s.reputation.GenericRepLevel;
import com.adamki11s.reputation.Reputation;
import com.adamki11s.reputation.ReputationManager;


public class DialogueItem {

	private final String say;
	private final GenericRepLevel requriedRep;
	private final Trigger trigger;
	
	//Implement triggers!

	public DialogueItem(String say, GenericRepLevel rep, Trigger trigger) {
		this.say = say;
		this.requriedRep = rep;
		this.trigger = trigger;
	}

	public String getSay() {
		return say;
	}

	public GenericRepLevel getRequriedRep() {
		return requriedRep;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public boolean doesPlayerHaveRequiredRepLevel(String pName) {
		if(this.requriedRep == GenericRepLevel.ANY){
			return true;
		}
		Reputation r = ReputationManager.getPlayerReputation(pName);
		int repPoints = r.getRep();
		boolean isNegRep = (repPoints < 0);
		ArrayList<GenericRepLevel> repList = new ArrayList<GenericRepLevel>(3);
		if (isNegRep) {
			do {
				repList.add(GenericRepLevel.getGenericReputation(repPoints));
				repPoints += 400;
			} while (repPoints < -200);
		} else {
			do {
				repList.add(GenericRepLevel.getGenericReputation(repPoints));
				repPoints -= 400;
			} while (repPoints > 200);
		}
		for(GenericRepLevel grl : repList){
			System.out.println("REP = " + grl.toString());
			if(grl.equals(this.requriedRep)){
				return true;
			}
		}
		return false;
	}

}
