package com.adamki11s.reputation;

import java.io.File;

import com.adamki11s.io.FileLocator;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class ReputationIO {

	public boolean doesPlayerHaveRepFile(String name) {
		return FileLocator.getPlayerRepFile(name).exists();
	}

	public void createPlayerRepFile(String name) {
		File f = FileLocator.getPlayerRepFile(name);
		SyncConfiguration cfg = new SyncConfiguration(f);
		cfg.createFileIfNeeded();
		cfg.add("R", 0);
		cfg.write();
	}

	public int getPlayerRep(String name) {
		File f = FileLocator.getPlayerRepFile(name);
		SyncConfiguration cfg = new SyncConfiguration(f);
		cfg.createFileIfNeeded();
		cfg.read();
		return cfg.getInt("R", 0);
	}

	public void updatePlayerRep(String name, int amount) {
		File f = FileLocator.getPlayerRepFile(name);
		SyncConfiguration cfg = new SyncConfiguration(f);
		cfg.read();
		int curAmt = cfg.getInt("R", 0);
		cfg.add("R", (curAmt + amount));
		cfg.write();
	}

}
