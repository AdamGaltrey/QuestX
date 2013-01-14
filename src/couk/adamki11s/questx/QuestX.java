package couk.adamki11s.questx;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import couk.adamki11s.commands.QuestXCommands;
import couk.adamki11s.data.Updater;
import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.threads.ThreadController;

public class QuestX extends JavaPlugin {

	Logger log = Logger.getLogger("QuestX");

	NPCHandler handle;
	ThreadController tControl;
	
	public static Plugin p;

	public NPCHandler getNPCHandler() {
		return this.handle;
	}

	@Override
	public void onEnable() {
		handle = new NPCHandler(this);
		p = this;
		this.getCommand("QuestX").setExecutor(new QuestXCommands(this));
		this.tControl = new ThreadController(handle);
		this.tControl.initiateAsyncThread(20L);
		//Updater updater = new Updater(this, "bukkitdev_slug", this.getFile(), Updater.UpdateType.DEFAULT, false);//Final boolean = show dl progress
	}

	@Override
	public void onDisable() {

	}

}
