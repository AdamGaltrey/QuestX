package com.adamki11s.updates;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.bundle.LocaleBundle;
import com.adamki11s.commands.QPerms;
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
		if (GeneralConfigData.isNotifyAdmin() && u.getResult() == UpdateResult.UPDATE_AVAILABLE && QPerms.hasPermission(p, "questx.update.notify")) {
			QuestX.logChat(p, ChatColor.GREEN + LocaleBundle.getString("version") + u.getLatestVersionString() + LocaleBundle.getString("new_ver_released") + QuestX.version);
			QuestX.logChat(p, ChatColor.GREEN + LocaleBundle.getString("run_cmd") + ChatColor.RED + "/questx force-update" + ChatColor.GREEN + LocaleBundle.getString("to_auto_update"));
		}
	}

}
