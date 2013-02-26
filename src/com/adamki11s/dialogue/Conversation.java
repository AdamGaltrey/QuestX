package com.adamki11s.dialogue;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.adamki11s.bundle.LocaleBundle;
import com.adamki11s.dialogue.triggers.Trigger;
import com.adamki11s.dialogue.triggers.TriggerType;
import com.adamki11s.display.StaticStrings;
import com.adamki11s.events.ConversationRegister;
import com.adamki11s.exceptions.InvalidDialogueException;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.npcs.tasks.NPCTalkTracker;
import com.adamki11s.npcs.tasks.TaskLoader;
import com.adamki11s.npcs.tasks.TaskManager;
import com.adamki11s.npcs.tasks.TaskRegister;
import com.adamki11s.quests.QuestLoader;
import com.adamki11s.quests.QuestManager;
import com.adamki11s.quests.QuestTask;
import com.adamki11s.questx.QuestX;

public class Conversation {

	ConversationData convoData;
	DialogueSet[] dialogue;
	String currentNode = "1";
	boolean conversing = false, indexSelected = false, parseSuccess = false;;

	final NPCHandler handle = new NPCHandler((JavaPlugin) QuestX.p, null);

	public void respond(String s) {
		int i = 1;
		try {
			i = Integer.parseInt(s);
			this.selectSpeechOption(i);
		} catch (NumberFormatException ex) {
			QuestX.logChat(this.convoData.getPlayer(), LocaleBundle.getString("invalid_index"));
		}
	}

	public Conversation(String pName, SimpleNPC npc) {
		this.convoData = new ConversationData(pName, npc);
	}

	public void loadConversation() {
		// Find file
		// Invoke DLG Parser
		DLGParser parse = new DLGParser(this, FileLocator.getNPCDlgFile(this.convoData.getSimpleNpc().getName()), this.convoData.getSimpleNpc().getName());
		try {
			this.dialogue = parse.parse();
			parseSuccess = true;
		} catch (InvalidDialogueException e) {
			parseSuccess = false;
			QuestX.logError("-----REASON-----");
			e.printErrorReason();
			/*
			 * QuestX.logError("-----STACK-----"); e.printStackTrace();
			 * QuestX.logError("-----STACK END-----");
			 */
		}
	}

	public boolean wasParseSuccessful() {
		return this.parseSuccess;
	}

	public void startConversation() {
		Player p = this.getConvoData().getPlayer();
		StringBuilder build = new StringBuilder();

		String npcName = this.convoData.getSimpleNpc().getName();

		boolean taskEnabled = TaskRegister.isTaskEnabled(npcName), questEnabled = this.convoData.getSimpleNpc().doesLinkToQuest();

		QuestX.logChat(p, ChatColor.GREEN + LocaleBundle.getString("convo_started") + ChatColor.ITALIC + "" + ChatColor.YELLOW + npcName);

		if (taskEnabled) {
			build.append(LocaleBundle.getString("task_indicator"));
			if (TaskRegister.hasPlayerCompletedTask(npcName, p.getName())) {
				build.append(ChatColor.GREEN).append(LocaleBundle.getString("complete"));
			} else {
				build.append(ChatColor.DARK_RED).append(LocaleBundle.getString("incomplete"));
			}
		}

		if (questEnabled) {
			build.append(ChatColor.RESET).append(LocaleBundle.getString("quest_indicator"));

			String qName = this.convoData.getSimpleNpc().getQuestName();

			if (QuestManager.hasQuestBeenSetup(qName)) {
				if (!QuestManager.isQuestLoaded(qName)) {
					QuestManager.loadQuest(qName);
				}
				QuestLoader ql = QuestManager.getQuestLoader(qName);
				ql.loadAndCheckPlayerProgress(p.getName());
				if (ql.isQuestComplete(p.getName())) {
					build.append(ChatColor.GREEN).append(LocaleBundle.getString("complete"));
				} else {
					build.append(ChatColor.DARK_RED).append(LocaleBundle.getString("incomplete"));
				}
			} else {
				build.append(ChatColor.DARK_RED).append(LocaleBundle.getString("not_setup"));
			}
		}

		if (build.toString().length() > 5) {
			QuestX.logChat(p, build.toString());
		}
		this.conversing = true;
		this.displaySpeechOptions();
		ConversationRegister.playersConversing.add(this);
	}

	public void endConversation() {
		if (this.conversing) {
			Player p = this.getConvoData().getPlayer();
			if (p != null) {
				QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("convo_ended"));
			}
			this.conversing = false;
			ConversationRegister.playersConversing.remove(this);
		}
	}

	public boolean isConversing() {
		return this.conversing;
	}

	public void displaySpeechOptions() {
		DialogueSet d = this.getDialogeSetFromNode(currentNode);
		DialogueItem[] items = d.getItems();
		Player p = this.convoData.getPlayer();
		int count = 1;
		QuestX.logChat(p, ChatColor.ITALIC + "" + ChatColor.BLUE + LocaleBundle.getString("speech_options") + ChatColor.RESET + StaticStrings.separator.substring(13));
		for (DialogueItem di : items) {
			if (di.doesPlayerHaveRequiredRepLevel(p.getName())) {
				QuestX.logChat(p, "[#" + count + "] " + di.getSay());
			} else {
				QuestX.logChat(p, "[#" + count + "] " + LocaleBundle.getString("option_unavailable"));
			}
			count += 1;
		}

	}

	public void selectSpeechOption(int index) {
		DialogueSet d = this.getDialogeSetFromNode(currentNode);
		DialogueItem[] items = d.getItems();
		if (index > items.length || index < 1) {
			QuestX.logChat(this.getConvoData().getPlayer(), LocaleBundle.getString("invalid_chat_option"));
			this.displaySpeechOptions();
			return;
		}
		DialogueItem selected = items[index - 1];
		Player p = this.convoData.getPlayer();
		String npcName = this.convoData.getSimpleNpc().getName();
		if (selected.doesPlayerHaveRequiredRepLevel(p.getName())) {
			Trigger selTrigger = selected.getTrigger();
			QuestX.logChat(p, ChatColor.ITALIC + "" + ChatColor.YELLOW + LocaleBundle.getString("response") + ChatColor.RESET + StaticStrings.separator.substring(8));
			if (selTrigger.getTriggerType() != TriggerType.QUEST) {
				DialogueResponse dr = d.getResponse();
				String response = dr.getResponses()[index - 1];
				QuestX.logChat(p, response);
			}

			this.currentNode = this.currentNode + index;
			if (selTrigger.getTriggerType() == TriggerType.END) {
				this.endConversation();
				return;
			} else if (selTrigger.getTriggerType() == TriggerType.TASK) {
				if (TaskRegister.isTaskEnabled(npcName)) {

					boolean alreadyDone = TaskRegister.hasPlayerCompletedTask(npcName, p.getName());
					if (alreadyDone) {
						QuestX.logChat(p, LocaleBundle.getString("task_already_completed"));
						this.endConversation();
						return;
					}
					if (TaskRegister.doesPlayerHaveTask(p.getName())) {
						QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("task_already_assigned"));
						QuestX.logChat(p, ChatColor.WHITE + "/questx task cancel" + ChatColor.RED + LocaleBundle.getString("cancel_current_task"));
						this.endConversation();
						return;
					} else {
						TaskLoader tl = new TaskLoader(FileLocator.getNPCTaskFile(npcName), npcName);

						tl.load();
						TaskManager manage = new TaskManager(p.getName(), tl);
						TaskRegister.registerTask(manage);
						QuestX.logChat(p, ChatColor.ITALIC + tl.getTaskName() + ChatColor.RESET + ChatColor.GREEN + " task started!");
						QuestX.logChat(p, LocaleBundle.getString("task_description_header") + tl.getTaskDescription());

						this.endConversation();
						return;
					}

				} else {
					QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("task_not_enabled"));
					this.endConversation();
					return;
				}
			} else if (selTrigger.getTriggerType() == TriggerType.QUEST) {
				SimpleNPC npc = this.getConvoData().getSimpleNpc();
				if (npc.doesLinkToQuest()) {
					String qName = npc.getQuestName();

					if (QuestManager.hasQuestBeenSetup(qName)) {

						if (!QuestManager.doesPlayerHaveQuest(p.getName())) {
							// start a quest
							if (!QuestManager.isQuestLoaded(qName)) {
								QuestManager.loadQuest(qName);
							}
							QuestLoader ql = QuestManager.getQuestLoader(qName);
							if (ql.canPlayerUndertakeQuest(p)) {
								ql.loadAndCheckPlayerProgress(p.getName());
								if (ql.isQuestComplete(p.getName())) {
									QuestX.logChat(p, LocaleBundle.getString("quest_already_completed"));
								} else {
									QuestManager.setCurrentPlayerQuest(p.getName(), qName);
									if (ql.getCurrentQuestNode(p.getName()) == 1) {
										QuestX.logChat(p, ql.getStartText());
									}
									QuestTask t = ql.getPlayerQuestTask(p.getName());
									t.sendWhatIsLeftToDo(p);
								}
								this.endConversation();
							} else {
								this.endConversation();
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
											track.setTalkedTo();
											ql.incrementTaskProgress(p);
											if (ql.isQuestComplete(p.getName())) {
												QuestX.logChat(p, ql.getEndText());
												QuestManager.removeCurrentPlayerQuest(ql.getName(), p.getName());
											}
										} else {
											QuestX.logChat(p, LocaleBundle.getString("need_to_speak_to") + track.getNPCName() + LocaleBundle.getString("to_complete_part_of_quest"));
											// wrong npc
										}

									} else {
										QuestX.logChat(p, LocaleBundle.getString("need_to_speak_to") + track.getNPCName() + LocaleBundle.getString("to_complete_part_of_quest"));
									}
								}

							}

							// player already has quest
						}
						this.endConversation();
					} else {
						// quest has not been setup
						QuestX.logChat(p, LocaleBundle.getString("quest_not_setup")+" /q setup " + qName);
						this.endConversation();
					}
				} else {
					this.endConversation();
				}
				this.endConversation();
			} else if (selTrigger.getTriggerType() == TriggerType.CUSTOM) {
				this.convoData.getSimpleNpc().invokeCustomActions(p);
				this.endConversation();
			} else if (selTrigger.getTriggerType() == TriggerType.CUSTOM_DEFINED) {
				this.convoData.getSimpleNpc().invokeCustomDefAction(p, selTrigger.getTriggerScript());
				this.endConversation();
				// load and cache custom actions
			} else {
				this.displaySpeechOptions();
			}
		} else {
			QuestX.logChat(p, LocaleBundle.getString("reputation_req") + items[index - 1].getRequriedRep().getMinRep() + LocaleBundle.getString("reputation"));
		}
	}

	boolean canNPCCompleteQuestNode(String quest, String player) {
		SimpleNPC n = this.getConvoData().getSimpleNpc();
		int currentNode = QuestManager.getQuestLoader(quest).getCurrentQuestNode(player);
		return (n.getCompleteQuestNodes().contains(currentNode));
	}

	DialogueSet getDialogeSetFromNode(String node) {
		for (DialogueSet dSet : dialogue) {
			if (dSet.getDialogueID().equalsIgnoreCase(node)) {
				return dSet;
			}
		}
		return null;
	}

	public ConversationData getConvoData() {
		return this.convoData;
	}

}
