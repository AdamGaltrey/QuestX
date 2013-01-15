package couk.adamki11s.io;

import java.io.File;

public class InitialSetup {
	
	public static void run(){
		folderSetup(new File(FileLocator.root));
		folderSetup(new File(FileLocator.config_root));
		folderSetup(new File(FileLocator.data_root));
		folderSetup(new File(FileLocator.npc_data_root));
	}
	
	static void folderSetup(File f){
		if(!f.exists()){
			System.out.println("Directory Created : " + f.getPath());
			f.mkdirs();
		}
	}
	
	static void fileSetup(File f){
		
	}

}
