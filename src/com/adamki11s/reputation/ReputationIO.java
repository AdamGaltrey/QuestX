package com.adamki11s.reputation;

import java.io.File;

import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class ReputationIO {
	
	public boolean doesPlayerHaveRepFile(String name){
		return FileLocator.getPlayerRepFile(name).exists();
	}
	
	public void createPlayerRepFile(String name){
		File f = FileLocator.getPlayerRepFile(name);
		SyncConfiguration cfg = new SyncConfiguration(f);
		cfg.createFileIfNeeded();
		cfg.add("R", 0);
		cfg.write();
	}
	
	public int getPlayerRep(String name){
		File f = FileLocator.getPlayerRepFile(name);
		SyncConfiguration cfg = new SyncConfiguration(f);
		cfg.createFileIfNeeded();
		cfg.read();
		if(cfg.doesKeyExist("R")){
			return cfg.getInt("R");
		} else {
			cfg.add("R", 0);
			cfg.write();
			return 0;
		}
	}
	
	public void updatePlayerRep(String name, int amount){
		QuestX.logMSG("Adding " + amount + " rep to " + name);
		File f = FileLocator.getPlayerRepFile(name);
		SyncConfiguration cfg = new SyncConfiguration(f);
		cfg.read();
		if(cfg.doesKeyExist("R")){
			int curAmt = cfg.getInt("R");
			cfg.add("R", (curAmt + amount));
			cfg.write();
		}
	}

}
