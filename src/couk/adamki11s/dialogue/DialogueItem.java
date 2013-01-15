package couk.adamki11s.dialogue;

import java.util.ArrayList;
import java.util.List;

import couk.adamki11s.ai.dataset.GenericRepLevel;
import couk.adamki11s.ai.dataset.Reputation;
import couk.adamki11s.dialogue.triggers.Trigger;

public class DialogueItem {

	final String say;
	final GenericRepLevel requriedRep;
	final Trigger trigger;
	
	//Implement triggers!

	public DialogueItem(String say, GenericRepLevel rep, Trigger trigger) {
		this.say = say;
		this.requriedRep = rep;
		this.trigger = trigger;
	}

	public String getString() {
		return this.say;
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

	/*
	 * 
	 * FORMAT ------
	 * DialogueSet_ID#DialogueAction#OptionsNumber#"speech1"#"speech2"
	 * #"speech3"#GTAG1,GTAG2,GTAG3#TRIG1,TRIG2,TRIG3 DialogueAction : say,
	 * reply Speech : String of text Generic Tag : e[evil], b[bad], o[ordinary],
	 * g[good], h[hero], a[any] Trigger Tag : n[none], q[quest], t[task],
	 * e[end_convo] ------
	 */
	
	/*
	 * 
	 * 
	 * 0#say#2#"Hello There."#"Bye!"#a,a#n,e
	 * 
	 * 0#reply#2#"Hi there %pname%, what can i do for you?","Clear out criminal"
	 * #a,b#n,e
	 * 
	 * 01#say#1#"Where am I?"#a#n
	 * 
	 * 01#reply#1#"The World of Minecraft"#a#n
	 * 
	 * 011#say#2#"Ok, thanks"#"Goodbye friend"#a,a#e,e
	 */

}
