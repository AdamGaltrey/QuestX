package com.adamki11s.quests.setup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;

public class QuestUnpacker {

	final String name, zipFile, tempExtract, finalLoc;
	boolean canQuestBeUnpacked;

	public QuestUnpacker(String name) {
		this.name = name;
		this.zipFile = FileLocator.quest_data_root + File.separator + name + ".zip";
		this.tempExtract = FileLocator.quest_data_root + File.separator + name + "_temp";
		this.finalLoc = FileLocator.quest_data_root + File.separator + name;
	}

	public boolean unpackQuest() {
		canQuestBeUnpacked = (!FileLocator.doesQuestNameExist(name));
		

		// check for duplicate quest
		if (!canQuestBeUnpacked) {
			QuestX.logMSG("UNPACKER ERROR--- Duplicate quest name");
			return false;
		}

		canQuestBeUnpacked = (new File(zipFile).exists());
		
		if (!canQuestBeUnpacked) {
			QuestX.logMSG("UNPACKER ERROR--- Packed quest does not exist");
			return false;
		}

		
		this.extractFolder(zipFile, tempExtract);

		// check for duplicate NPC names
		for (File f : new File(tempExtract + File.separator + "NPCs").listFiles()) {
			if (f.isDirectory() && FileLocator.doesNPCNameExist(f.getName())) {
				QuestX.logMSG("NPC name '" + f.getName() + "' already exists. Please rename the NPC.");
				QuestX.logMSG("UNPACKER ERROR--- Duplicate NPC name");
				try {
					this.deleteDirectory(new File(tempExtract));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}
		
		if(!new File(finalLoc).exists()){
			new File(finalLoc).mkdirs();
		}

		File srcFolder = new File(tempExtract + File.separator + "NPCs"), destFolder = new File(FileLocator.npc_data_root), srcQFolder = new File(tempExtract + File.separator + "Quest"), destQFolder = new File(
				finalLoc);

		//copy temp content to respective directories
		try {
			copyFolder(srcFolder, destFolder);
			copyFolder(srcQFolder, destQFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//copy zip file into quest folder as a backup
		this.copyFile(new File(zipFile), new File(FileLocator.quest_data_root + File.separator + name + File.separator + name + ".zip"), true);
		
		//delete temporary folder and zip file
		try {
			this.deleteDirectory(new File(tempExtract));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new File(zipFile).delete();

		System.out.println("Done");

		return true;
	}

	private void extractFolder(String zipFile, String extractFolder) {
		try {
			QuestX.logMSG("Unpacking Quest '" + name + "' files.");
			int BUFFER = 2048;
			File file = new File(zipFile);

			ZipFile zip = new ZipFile(file);
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
					is.close();
				}

			}
			
			zip.close();

			QuestX.logMSG("Files unpacked successfully.");
		} catch (Exception e) {
			QuestX.logMSG("Error encountered while unpacking Quest '" + name + "' " + e.getMessage());
		}

	}

	private void copyFolder(File src, File dest) throws IOException {

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

	private void copyFile(File source, File dest, boolean deleteOnCopy) {

		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			
			if(!dest.exists()){
				dest.createNewFile();
			}

			inStream = new FileInputStream(source);
			outStream = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

			if(deleteOnCopy){
				source.delete();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	 private void deleteDirectory(File file)
 	throws IOException{

 	if(file.isDirectory()){

 		//directory is empty, then delete it
 		if(file.list().length==0){

 		   file.delete();
 		   System.out.println("Directory is deleted : " 
                                              + file.getAbsolutePath());

 		}else{

 		   //list all the directory contents
     	   String files[] = file.list();

     	   for (String temp : files) {
     	      //construct the file structure
     	      File fileDelete = new File(file, temp);

     	      //recursive delete
     	     deleteDirectory(fileDelete);
     	   }

     	   //check the directory again, if empty then delete it
     	   if(file.list().length==0){
        	     file.delete();
     	     System.out.println("Directory is deleted : " 
                                               + file.getAbsolutePath());
     	   }
 		}

 	}else{
 		//if file, then delete it
 		file.delete();
 		System.out.println("File is deleted : " + file.getAbsolutePath());
 	}
 }
	
}
