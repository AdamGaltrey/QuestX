package com.adamki11s.npcs.triggers.action;

import org.bukkit.entity.Player;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.npcs.tasks.NPCTalkTracker;
import com.adamki11s.quests.QuestLoader;
import com.adamki11s.quests.QuestManager;
import com.adamki11s.quests.QuestTask;
import com.adamki11s.questx.QuestX;

public class InvokeQuestAction implements Action {

	private final String npc;

	private final NPCHandler handle;

	private boolean isActive;

	public InvokeQuestAction(NPCHandler handle, String npc) {
		this.handle = handle;
		this.npc = npc;
	}
	
	public boolean canPlayerTriggerQuest(Player p){
		SimpleNPC npc = this.handle.getSimpleNPCByName(this.npc);
		if (npc == null) {
			this.isActive = false;
			QuestX.logError("Null npc for custom_trigger quest invokation " + this.npc);
			return false;
		} else {
			if (npc.doesLinkToQuest()) {

				String qName = npc.getQuestName();

				if (QuestManager.hasQuestBeenSetup(qName)) {

					if (!QuestManager.doesPlayerHaveQuest(p.getName())) {
						// start a quest
						QuestX.logDebug("Player does not have quest!");
						if (!QuestManager.isQuestLoaded(qName)) {
							QuestManager.loadQuest(qName);
						}
						QuestX.logDebug("QUEST LOADED ############");
						QuestLoader ql = QuestManager.getQuestLoader(qName);
						ql.loadAndCheckPlayerProgress(p.getName());
						if (ql.isQuestComplete(p.getName())) {
							QuestX.logChat(p, "You have already completed this quest!");
							return false;
						} else {
							return true;
						}
					} else {
						
						if (npc.doesLinkToQuest()) {

							QuestTask qt = QuestManager.getCurrentQuestTask(p.getName());

							if (qt.isTalkNPC()) {
								QuestLoader ql = QuestManager.getQuestLoader(QuestManager.getCurrentQuestName(p.getName()));
								int curNode = ql.getCurrentQuestNode(p.getName());
								NPCTalkTracker track = (NPCTalkTracker) qt.getData();
								if (npc.getCompleteQuestNodes().contains(curNode)) {
									// npc can complete

									if (track.getNPCName().equalsIgnoreCase(npc.getName())) {
										// correct npc
										return true;
									} else {
										QuestX.logChat(p, "You need to speak to '" + track.getNPCName() + "' to complete this part of your quest.");
										return false;
										// wrong npc
									}

								} else {
									QuestX.logChat(p, "You need to speak to '" + track.getNPCName() + "' to complete this part of your quest.");
									return false;
								}
							}

						} else {
							return false;
						}

						// player already has quest
					}
				} else {
					// quest has not been setup
					QuestX.logChat(p, "This quest has not yet been setup. /q setup " + qName);
					return false;
				}
			} else {
				QuestX.logDebug("NPC has no link to a quest");
				return false;
			}
			return false;
		}
	}

	@Override
	public void implement(Player p) {
		SimpleNPC npc = this.handle.getSimpleNPCByName(this.npc);
		if (npc == null) {
			this.isActive = false;
			QuestX.logError("Null npc for custom_trigger quest invokation " + this.npc);
			return;
		} else {
			if (npc.doesLinkToQuest()) {

				String qName = npc.getQuestName();

				if (QuestManager.hasQuestBeenSetup(qName)) {

					if (!QuestManager.doesPlayerHaveQuest(p.getName())) {
						// start a quest
						QuestX.logDebug("Player does not have quest!");
						if (!QuestManager.isQuestLoaded(qName)) {
							QuestManager.loadQuest(qName);
						}
						QuestX.logDebug("QUEST LOADED ############");
						QuestLoader ql = QuestManager.getQuestLoader(qName);
						ql.loadAndCheckPlayerProgress(p.getName());
						if (ql.isQuestComplete(p.getName())) {
							QuestX.logChat(p, "You have already completed this quest!");
						} else {
							QuestManager.setCurrentPlayerQuest(p.getName(), qName);
							QuestX.logDebug(ql.getStartText() + "<<<<<< START TEXT");
							QuestX.logChat(p, ql.getStartText());
							QuestTask t = QuestManager.getCurrentQuestTask(p.getName());
							if (t != null) {
								QuestX.logDebug("Task in non-null");
								// t.sendWhatIsLeftToDo(p);
							} else {
								QuestX.logDebug("Task is null!");
							}
						}
					} else {
						QuestX.logDebug("Player has quest!");
						if (npc.doesLinkToQuest()) {

							QuestTask qt = QuestManager.getCurrentQuestTask(p.getName());

							if (qt.isTalkNPC()) {
								QuestLoader ql = QuestManager.getQuestLoader(QuestManager.getCurrentQuestName(p.getName()));
								int curNode = ql.getCurrentQuestNode(p.getName());
								NPCTalkTracker track = (NPCTalkTracker) qt.getData();
								if (npc.getCompleteQuestNodes().contains(curNode)) {
									// npc can complete

									if (track.getNPCName().equalsIgnoreCase(npc.getName())) {
										// correct npc
										track.setTalkedTo();
										ql.incrementTaskProgress(p);
										if (ql.isQuestComplete(p.getName())) {
											QuestX.logChat(p, ql.getEndText());
											QuestManager.removeCurrentPlayerQuest(ql.getName(), p.getName());
										}
									} else {
										QuestX.logChat(p, "You need to speak to '" + track.getNPCName() + "' to complete this part of your quest.");
										// wrong npc
									}

								} else {
									QuestX.logChat(p, "You need to speak to '" + track.getNPCName() + "' to complete this part of your quest.");
								}
							}

						}

						// player already has quest
					}
				} else {
					// quest has not been setup
					QuestX.logChat(p, "This quest has not yet been setup. /q setup " + qName);
				}
			} else {
				QuestX.logDebug("NPC has no link to a quest");
			}
		}
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
