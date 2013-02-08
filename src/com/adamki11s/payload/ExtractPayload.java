package com.adamki11s.payload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.adamki11s.io.FileLocator;
import com.adamki11s.quests.setup.QuestUnpacker;
import com.adamki11s.questx.QuestX;

public class ExtractPayload {

	public static void extractPayload() {
		
		QuestX.logMSG("Checking payload state...");

		final String fs = File.separator;
		InputStream stream = ExtractPayload.class.getResourceAsStream("/res/npc_payload.zip");
		OutputStream resStreamOut;
		int readBytes;
		byte[] buffer = new byte[4096];
		try {
			resStreamOut = new FileOutputStream(new File(FileLocator.npc_data_root + fs + "npc_payload.zip"));

			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
			resStreamOut.flush();
			resStreamOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		stream = ExtractPayload.class.getResourceAsStream("/res/quest_payload.zip");

		try {
			resStreamOut = new FileOutputStream(new File(FileLocator.quest_data_root + fs + "quest_payload.zip"));
			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
			resStreamOut.flush();
			resStreamOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		final String zipQ = FileLocator.quest_data_root + File.separator + "quest_payload.zip", zipNPC = FileLocator.npc_data_root + File.separator + "npc_payload.zip";
		final File qFile = new File(zipQ), npcFile = new File(zipNPC);

		int questMods = extractFolder(zipQ, FileLocator.quest_data_root);

		int npcMods = extractFolder(zipNPC, FileLocator.npc_data_root);

		// delete original payload extract
		qFile.delete();
		npcFile.delete();

		// unpack quests
		for (File f : new File(FileLocator.quest_data_root).listFiles()) {
			if (f.getName().endsWith(".zip")) {
				String qName = f.getName().substring(0, f.getName().lastIndexOf("."));
				
				File questFileLocal = new File(FileLocator.quest_data_root + File.separator + qName);
				
				if(questFileLocal.exists()){
					continue;
				}
				
				QuestUnpacker upack = new QuestUnpacker(qName);
				boolean suc = upack.unpackQuest();
				if (!suc) {
					QuestX.logError("Error unpacking quest " + qName);
				}
			}
		}

		if(questMods == 0 && npcMods == 0){
			//everything went fine
			QuestX.logMSG("Payload is up to date.");
		} else {
			QuestX.logMSG("Payload updated. NPC File Modifications = " + npcMods + ", Quest file modifications = " + questMods);
		}

	}

	private static int extractFolder(String zipFile, String extractFolder) {
		ZipFile zip = null;
		int modifications = 0;
		try {
			int BUFFER = 2048;
			File file = new File(zipFile);

			zip = new ZipFile(file);

			String newPath = extractFolder;

			
			
			Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();
			
			boolean needToMakePath = true;

			while (zipFileEntries.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();

				File destFile = new File(newPath, currentEntry);

				//Don't overwrite existing file
				if (destFile.exists()) {
					continue;
				}
				
				if(needToMakePath){
					//switches boolean value (switch to false)
					needToMakePath ^= true;
					new File(newPath).mkdir();
				}
				
				modifications++;

				File destinationParent = destFile.getParentFile();

				destinationParent.mkdirs();

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					byte data[] = new byte[BUFFER];

					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					fos.flush();
					fos.close();
					is.close();
				}

			}

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (zip != null) {
					zip.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return modifications;
	}

	/*private static void CopyFolder() {
		try {
			File dir = new File("plugins/QuestX/Data/NPCs");
			dir.mkdirs();
			File resource = new File(new URI(ExtractPayload.class.getClass().getResource("/res/npcs").toString()));
			File[] listResource = resource.listFiles();
			String[] files = resource.list();
			for (int i = 0; i < files.length; i++) {
				File dstfile1 = new File(dir, files[i]);
				FileInputStream is1 = new FileInputStream(listResource[i]);
				FileOutputStream fos1 = new FileOutputStream(dstfile1);
				int b1;
				while ((b1 = is1.read()) != -1) {
					fos1.write(b1);
				}
				fos1.close();
			}
		} catch (Exception e) {
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
	}*/

}
