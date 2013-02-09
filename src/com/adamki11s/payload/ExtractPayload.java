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

	private static boolean startup = true;

	public static void extractPayload() {

		if (startup) {
			// invert boolean
			startup ^= true;
		} else {
			// only check payload on server stars, not reloads.
			return;
		}

		QuestX.logMSG("Checking payload state...");

		final String fs = File.separator;
		InputStream stream = ExtractPayload.class.getResourceAsStream("/res/npc_payload.zip");
		OutputStream resStreamOut;
		int readBytes;
		byte[] buffer = new byte[4096];

		if (stream == null) {
			// breaks out of startup check if server reloads with a newer
			// version
			return;
		}

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

		final String zipQ = FileLocator.quest_data_root + File.separator + "quest_payload.zip", zipNPC = FileLocator.npc_data_root + File.separator + "npc_payload.zip";
		final File qFile = new File(zipQ), npcFile = new File(zipNPC);

		int npcMods = extractFolder(zipNPC, FileLocator.npc_data_root);

		npcFile.delete();

		stream = ExtractPayload.class.getResourceAsStream("/res/quest_payload.zip");

		if (stream == null) {
			// breaks out of startup check if server reloads with a newer
			// version
			if (npcMods != 0) {
				QuestX.logMSG("NPC File modifications = " + npcMods);
			}
			QuestX.logMSG("Payload is up to date.");
			return;
		}

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

		int questMods = extractFolder(zipQ, FileLocator.quest_data_root);

		// delete original payload extract
		qFile.delete();

		// unpack quests
		for (File f : new File(FileLocator.quest_data_root).listFiles()) {
			if (f.getName().endsWith(".zip")) {
				String qName = f.getName().substring(0, f.getName().lastIndexOf("."));

				File questFileLocal = new File(FileLocator.quest_data_root + File.separator + qName);

				if (questFileLocal.exists()) {
					continue;
				}

				QuestUnpacker upack = new QuestUnpacker(qName);
				boolean suc = upack.unpackQuest();
				if (!suc) {
					QuestX.logError("Error unpacking quest " + qName);
				}
			}
		}

		if (questMods == 0 && npcMods == 0) {
			// everything went fine
			QuestX.logMSG("Payload is up to date.");
		} else {
			QuestX.logMSG("Payload updated. NPC File Modifications = " + npcMods + ", Quest file modifications = " + questMods);
			QuestX.logMSG("Payload is up to date.");
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

				// Don't overwrite existing file
				if (destFile.exists()) {
					continue;
				}

				if (needToMakePath) {
					// switches boolean value (switch to false)
					needToMakePath ^= true;
					
					//create need directory
					new File(newPath).mkdirs();
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
				} else {
					destFile.mkdirs();
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

}
