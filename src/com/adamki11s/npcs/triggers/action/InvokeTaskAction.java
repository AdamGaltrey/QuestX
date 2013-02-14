package com.adamki11s.npcs.triggers.action;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.exceptions.MissingTaskPropertyException;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.tasks.TaskLoader;
import com.adamki11s.npcs.tasks.TaskManager;
import com.adamki11s.npcs.tasks.TaskRegister;
import com.adamki11s.questx.QuestX;

public class InvokeTaskAction implements Action{
	
	private final String npc;
	
	private boolean isActive;
	
	public InvokeTaskAction(String npc){
		this.npc = npc;
	}
	
	public boolean canPlayerTriggerTask(Player p){
		boolean alreadyDone = TaskRegister.hasPlayerCompletedTask(this.npc, p.getName());
		if (alreadyDone) {
			QuestX.logChat(p, "You have already completed this task!");
			return false;
		}
		if (TaskRegister.doesPlayerHaveTask(p.getName())) {
			QuestX.logChat(p, ChatColor.RED + "You already have a task assigned!");
			QuestX.logChat(p, ChatColor.WHITE + "/questx task cancel" + ChatColor.RED + " to cancel current task.");
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void implement(Player p) {
		boolean alreadyDone = TaskRegister.hasPlayerCompletedTask(this.npc, p.getName());
		if (alreadyDone) {
			QuestX.logChat(p, "You have already completed this task!");
			return;
		}
		if (TaskRegister.doesPlayerHaveTask(p.getName())) {
			QuestX.logChat(p, ChatColor.RED + "You already have a task assigned!");
			QuestX.logChat(p, ChatColor.WHITE + "/questx task cancel" + ChatColor.RED + " to cancel current task.");
			return;
		} else {
			TaskLoader tl = new TaskLoader(FileLocator.getNPCTaskFile(this.npc), this.npc);
			try {
				tl.load();
				QuestX.logDebug("Task Loaded!");
				TaskManager manage = new TaskManager(p.getName(), tl);
				TaskRegister.registerTask(manage);
				QuestX.logChat(p, ChatColor.ITALIC + tl.getTaskName() + ChatColor.RESET + ChatColor.GREEN + " task started!");
				QuestX.logChat(p, "Task description : " + tl.getTaskDescription());
			} catch (MissingTaskPropertyException e) {
				this.isActive = false;
				e.printErrorReason();
				QuestX.logChatError(p, "Task failed to load, task file is incorrectly formatted. Check the server log for details.");
			}
			return;
		}
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
