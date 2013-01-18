package couk.adamki11s.npcs.loading;

import couk.adamki11s.io.FileLocator;
import couk.adamki11s.sync.io.configuration.SyncConfiguration;

public class FixedLoadingTable {
	
	public static void registerFixedNPCSpawns(){
		SyncConfiguration loader = new SyncConfiguration(FileLocator.getNPCFixedSpawnsFile());
		loader.read();
		
		//FORMAT = <npcname>:
	}

}
