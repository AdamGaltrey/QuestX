package com.adamki11s.updates;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.io.GeneralConfigData;
import com.adamki11s.questx.QuestX;
import com.adamki11s.updates.Updater.UpdateResult;

public class UpdateNotifier {

	static Updater u;

	public static void setNotifier(Updater up) {
		u = up;
	}

	public static void onPlayerLogin(Player p) {
		// if player is admin
		if (GeneralConfigData.isNotifyAdmin() && u.getResult() == UpdateResult.UPDATE_AVAILABLE && QuestX.permission.has(p, "questx.admin.*")) {
			QuestX.logChat(p, ChatColor.GREEN + "Version " + u.getLatestVersionString() + " of QuestX has been released, you are running version " + QuestX.version);
			QuestX.logChat(p, ChatColor.GREEN + "Run command " + ChatColor.RED + "/questx force-update" + ChatColor.GREEN + " to automatically update the plugin if you wish to do so.");
		}
	}

}
