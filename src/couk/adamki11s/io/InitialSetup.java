package couk.adamki11s.io;

import java.io.File;
import java.io.IOException;

public class InitialSetup {
	
	public static void run(){
		folderSetup(new File(FileLocator.root));
		folderSetup(new File(FileLocator.config_root));
		folderSetup(new File(FileLocator.data_root));
		folderSetup(new File(FileLocator.npc_data_root));
		fileSetup(FileLocator.getNPCFixedSpawnsFile());
	}
	
	static void folderSetup(File f){
		if(!f.exists()){
			System.out.println("Directory Created : " + f.getPath());
			f.mkdirs();
		}
	}
	
	static void fileSetup(File f){
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
