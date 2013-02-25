package com.adamki11s.poll;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.adamki11s.io.FileLocator;

public class FilePoll {

	public void poll() {
		// run every hour

		Date present = new Date();

		for (Entry<String, Integer> ent : PollManager.taskPoll.entrySet()) {
			File npcRoot = FileLocator.getNPCRootDir(ent.getKey());
			if (!npcRoot.exists()) {
				continue;
			} else {
				File progRoot = new File(npcRoot + File.separator + "Progression");
				for (File f : progRoot.listFiles()) {
					Date cur = new Date(f.lastModified());
					long milliSecTime = (present.getTime() - cur.getTime());
					int hoursDiff = (int) Math.floor((milliSecTime) / (3600000L));
					System.out.println("Hours diff for NPC's " + ent.getKey() + " task = " + hoursDiff);
					if (hoursDiff >= ent.getValue()) {
						// erase file
						f.delete();
					}
				}
			}
		}

		for (Entry<String, Integer> ent : PollManager.questPoll.entrySet()) {
			File questRoot = new File(FileLocator.quest_data_root);
			System.out.println("Checking quest root = " + ent.getKey());
			if (!questRoot.exists()) {
				System.out.println("none existant quest root, returning");
				continue;
			} else {
				System.out.println("Checking main quest folder exists");
				File root = new File(FileLocator.quest_data_root + File.separator + ent.getKey());
				if (!root.exists()) {
					System.out.println("main folder not found, exiting " + ent.getKey());
					continue;
				} else {
					System.out.println("Checking progression folder");
					File prog = new File(root + File.separator + "Progression");
					for (File f : prog.listFiles()) {
						Date cur = new Date(f.lastModified());
						long milliSecTime = (present.getTime() - cur.getTime());
						int hoursDiff = (int) Math.floor((milliSecTime) / (3600000L));
						System.out.println("Hours diff for quest " + ent.getKey() + " = " + hoursDiff);
						if (hoursDiff >= ent.getValue()) {
							// erase file
							f.delete();
						}
					}
				}

			}
		}

	}
}
