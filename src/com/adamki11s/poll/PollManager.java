package com.adamki11s.poll;

import java.util.HashMap;
import java.util.Map.Entry;

import com.adamki11s.io.FileLocator;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class PollManager {
	
	public static HashMap<String, Integer> taskPoll = new HashMap<String, Integer>();
	public static HashMap<String, Integer> questPoll = new HashMap<String, Integer>();

	public static void load() {
		SyncConfiguration c = new SyncConfiguration(FileLocator.getPollFile());
		c.read();
		for(Entry<String, Object> e : c.getReadableData().entrySet()){
			String type = e.getKey();
			String[] parts;
			int hours;
			if(type.equalsIgnoreCase("quest")){
				parts = e.getValue().toString().split(",");
				try{
					hours = Integer.parseInt(parts[1]);
					questPoll.put(parts[0].trim(), hours);
				} catch (NumberFormatException ex){
					continue;
				}
			} else if(type.equalsIgnoreCase("task")){
				parts = e.getValue().toString().split(",");
				try{
					hours = Integer.parseInt(parts[1]);
					taskPoll.put(parts[0].trim(), hours);
				} catch (NumberFormatException ex){
					continue;
				}
			}
		}
	}
}
