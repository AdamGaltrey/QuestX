package com.adamki11s.io;

import java.io.File;

import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class GeneralConfigData {

	static boolean tagAPISupport, checkUpdates, autoDLUpdates;

	public static void load() {
		File genConfig = FileLocator.getGeneralConfig();
		SyncConfiguration conf = new SyncConfiguration(genConfig);
		conf.read();
		tagAPISupport = conf.getBoolean("TAG_API_SUPPORT");
		checkUpdates = conf.getBoolean("CHECK_UPDATES");
		autoDLUpdates = conf.getBoolean("AUTO_DOWNLOAD_UPDATES");
	}

	public static boolean isTagAPISupported() {
		return tagAPISupport;
	}
	
	public static boolean isCheckingUpdates() {
		return checkUpdates;
	}
	
	public static boolean isAutoDLUpdates() {
		return autoDLUpdates;
	}

}
