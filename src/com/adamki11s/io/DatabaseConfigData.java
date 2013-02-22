package com.adamki11s.io;

import java.io.File;

import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class DatabaseConfigData {

	static int updateMinutes = 3;

	public static void load() {
		File dbconfig = FileLocator.getDatabaseConfig();
		SyncConfiguration conf = new SyncConfiguration(dbconfig);
		conf.read();
		updateMinutes = conf.getInt("LOGGING_FREQUENCY_MINUTES", 3);
	}

	public static int getUpdateMinutes(){
		return updateMinutes;
	}
	
}
