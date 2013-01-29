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
		
		File extractToggle = new File(FileLocator.data_root + File.separator + "payload.pyl");
		
		if(!extractToggle.exists()){

		QuestX.logMSG("Extracting npc_payload.zip");

		final String fs = File.separator;
		// InputStream stream =
		// ExtractPayload.class.getResourceAsStream("/res/npc/npc_payload.zip");//note
		// that each / is a directory down in the "jar tree" been the jar the
		// root of the tree"
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

		QuestX.logMSG("Extracting quest_payload.zip");

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
		

		QuestX.logMSG("Unzipping quest_payload.zip...");

		final String zipQ = FileLocator.quest_data_root + File.separator + "quest_payload.zip", zipNPC = FileLocator.npc_data_root + File.separator + "npc_payload.zip";
		final File qFile = new File(zipQ), npcFile = new File(zipNPC);

		extractFolder(zipQ, FileLocator.quest_data_root);

		QuestX.logMSG("Unzipping npc_payload.zip...");

		extractFolder(zipNPC, FileLocator.npc_data_root);

		// delete original payload extract
		qFile.delete();
		npcFile.delete();
		

		// unpack quests
		for (File f : new File(FileLocator.quest_data_root).listFiles()) {
			if (f.getName().endsWith(".zip")) {
				String qName = f.getName().substring(0, f.getName().lastIndexOf("."));
				QuestX.logError("Unpacking ------" + qName);
				QuestUnpacker upack = new QuestUnpacker(qName);
				boolean suc = upack.unpackQuest();
				if (!suc) {
					QuestX.logError("Error unpacking quest " + qName);
				}
			}
		}
		
		
		

		QuestX.logMSG("Internal files unpacked successfully.");
		
		try {
			extractToggle.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		} else {
			QuestX.logMSG("Payload has already been unpacked.");
		}

	}

	private static void extractFolder(String zipFile, String extractFolder) {
		ZipFile zip = null;
		try {
			// QuestX.logMSG("Unpacking Quest '" + name + "' files.");
			int BUFFER = 2048;
			File file = new File(zipFile);

			zip = new ZipFile(file);

			String newPath = extractFolder;

			new File(newPath).mkdir();
			Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

			// Process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();

				File destFile = new File(newPath, currentEntry);
				// destFile = new File(newPath, destFile.getName());
				File destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

					// read and write until last byte is encountered
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
			
		

			//QuestX.logMSG("Zip closed!");
			//zip.close();

			QuestX.logMSG("Files unpacked successfully.");
		} catch (Exception e) {
			// QuestX.logMSG("Error encountered while unpacking Quest '" + name
			// + "' " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (zip != null) {
					QuestX.logMSG("Zip closed in FINALLY!");
					zip.close();
				} else {
					QuestX.logMSG("Zip was null :(!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void CopyFolder() {
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
	}

}
