package couk.adamki11s.dialogue.triggers;

public enum TriggerType {
	
	NONE,
	QUEST,
	TASK,
	END;
	
	public static TriggerType parseTriggerType(String s){
		if(s.equalsIgnoreCase("n")){
			return NONE;
		} else if(s.equalsIgnoreCase("e")){
			return END;
		} else if(s.equalsIgnoreCase("t")){
			return TASK;
		} else {
			return QUEST;
		}
	}

}
