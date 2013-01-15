package couk.adamki11s.dialogue.triggers;

import java.io.File;

public abstract class Trigger {
	
	final TriggerType type;
	File triggerScript;
	
	public Trigger(TriggerType type){
		this.type = type;
	}
	
	public Trigger(TriggerType type, File triggerScript){
		this.type = type;
		this.triggerScript = triggerScript;
	}
	
	public boolean doesTriggerHaveScript(){
		return (this.type == TriggerType.QUEST || this.type == TriggerType.TASK);
	}
	
	public File getTriggerScript(){
		return this.triggerScript;
	}
	
	public TriggerType getTriggerType(){
		return this.type;
	}
	
	

}
