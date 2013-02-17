package com.adamki11s.dialogue.triggers;

public enum TriggerType {

	NONE, CUSTOM, CUSTOM_DEFINED, QUEST, TASK, END;

	public static TriggerType parseTriggerType(String s) {

		// c{myfile.qxs}
		String customTriggerDef = "^(c\\{)(.+)(.qxs\\})$";

		if (s.equalsIgnoreCase("n") || s.equalsIgnoreCase("none")) {
			return NONE;
		} else if (s.equalsIgnoreCase("e") || s.equalsIgnoreCase("end")) {
			return END;
		} else if (s.equalsIgnoreCase("t") || s.equalsIgnoreCase("task")) {
			return TASK;
		} else if (s.equalsIgnoreCase("q") || s.equalsIgnoreCase("quest")) {
			return QUEST;
		} else if (s.equalsIgnoreCase("c") || s.equalsIgnoreCase("custom")) {
			return CUSTOM;
		} else if (s.matches(customTriggerDef)) {
			return CUSTOM_DEFINED;
		} else {
			return null;
		}
	}
}
