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
					if (hoursDiff >= ent.getValue()) {
						// erase file
						f.delete();
					}
				}
			}
		}

		for (Entry<String, Integer> ent : PollManager.questPoll.entrySet()) {
			File questRoot = new File(FileLocator.quest_data_root);
			if (!questRoot.exists()) {
				continue;
			} else {
				File root = new File(FileLocator.quest_data_root + File.separator + ent.getKey());
				if (!root.exists()) {
					continue;
				} else {
					File prog = new File(root + File.separator + "Progression");
					for (File f : prog.listFiles()) {
						Date cur = new Date(f.lastModified());
						long milliSecTime = (present.getTime() - cur.getTime());
						int hoursDiff = (int) Math.floor((milliSecTime) / (3600000L));
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
