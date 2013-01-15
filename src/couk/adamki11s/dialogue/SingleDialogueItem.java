package couk.adamki11s.dialogue;

import java.util.ArrayList;

import couk.adamki11s.ai.dataset.GenericRepLevel;
import couk.adamki11s.ai.dataset.Reputation;
import couk.adamki11s.dialogue.triggers.Trigger;
import couk.adamki11s.dialogue.triggers.TriggerType;

public class SingleDialogueItem {
	
	final String say;
	final GenericRepLevel requriedRep;
	final TriggerType trigger;
	
	//Implement triggers!

	public SingleDialogueItem(String say, GenericRepLevel rep, TriggerType trigger) {
		this.say = say;
		this.requriedRep = rep;
		this.trigger = trigger;
	}

	public String getString() {
		return this.say;
	}

	public boolean doesPlayerHaveRequiredRepLevel(String pName) {
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

	/*
	 * 
	 * FORMAT ------
	 * DialogueSet_ID#DialogueAction#OptionsNumber#"speech1"#"speech2"
	 * #"speech3"#GTAG1,GTAG2,GTAG3#TRIG1,TRIG2,TRIG3 DialogueAction : say,
	 * reply Speech : String of text Generic Tag : e[evil], b[bad], o[ordinary],
	 * g[good], h[hero], a[any] Trigger Tag : n[none], q[quest], t[task],
	 * e[end_convo] ------
	 */

}
