package couk.adamki11s.dialogue;

import java.util.ArrayList;
import java.util.List;

import couk.adamki11s.ai.dataset.GenericRepLevel;
import couk.adamki11s.ai.dataset.Reputation;
import couk.adamki11s.dialogue.triggers.Trigger;

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
		Reputation r = Reputation.getPlayerReputation(pName);
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
