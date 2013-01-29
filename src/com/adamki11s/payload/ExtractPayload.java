package com.adamki11s.payload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.bukkit.Bukkit;

import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;

public class ExtractPayload {
	
	public static void extractPayload(){	
		
		QuestX.logMSG("Extracting npc_payload.zip");
		
		final String fs = File.separator;
		//InputStream stream = ExtractPayload.class.getResourceAsStream("/res/npc/npc_payload.zip");//note that each / is a directory down in the "jar tree" been the jar the root of the tree"
		InputStream stream = ExtractPayload.class.getResourceAsStream("/res/npc_payload.zip");
	    OutputStream resStreamOut;
	    int readBytes;
	    byte[] buffer = new byte[4096];
	    try {
	        resStreamOut = new FileOutputStream(new File(FileLocator.npc_data_root + fs + "npc_payload.zip"));
	        while ((readBytes = stream.read(buffer)) > 0) {
	            resStreamOut.write(buffer, 0, readBytes);
	        }
	    } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
	    QuestX.logMSG("Extracting quest_payload.zip");
	    
	    stream = ExtractPayload.class.getResourceAsStream("/res/quest_payload.zip");;
	    
	    try {
	        resStreamOut = new FileOutputStream(new File(FileLocator.quest_data_root + fs + "quest_payload.zip"));
	        while ((readBytes = stream.read(buffer)) > 0) {
	            resStreamOut.write(buffer, 0, readBytes);
	        }
	    } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
		
	}
	
	private static void CopyFolder()
	{
		try
		{
			File dir = new File("plugins/QuestX/Data/NPCs");  
			dir.mkdirs();				
			File resource = new File(new URI(ExtractPayload.class.getClass().getResource("/res/npcs").toString()));
			File[] listResource = resource.listFiles();
			String[] files=resource.list();
			for (int i = 0; i < files.length; i++) 
			{
				File dstfile1=new File(dir,files[i]);
				FileInputStream is1 = new FileInputStream(listResource[i]);
				FileOutputStream fos1 = new FileOutputStream(dstfile1);
				int b1;
				while((b1 = is1.read()) != -1) 
				{
					fos1.write(b1);
				}
				fos1.close();
			}  
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to " + dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}
	}

}
