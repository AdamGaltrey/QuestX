package com.adamki11s.npcs.triggers;

import java.io.File;
import java.io.IOException;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

import com.adamki11s.commands.QuestXCMDExecutor;
import com.adamki11s.exceptions.MissingDeathTriggerPropertyException;
import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;
import com.adamki11s.reputation.ReputationManager;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class DeathAction {

	final String npcName;

	String[] playerCMD, serverCMD;
	int awardRep, awardGold, awardExp;

	public DeathAction(String npcName) {
		this.npcName = npcName;
	}

	public void load() throws MissingDeathTriggerPropertyException {
		File f = FileLocator.getNPCDeathTriggerFile(npcName);
		SyncConfiguration io = new SyncConfiguration(f);
		
		if(!f.exists()){
			try {
				f.createNewFile();
				io.add("REWARD_EXP", 0);
				io.add("REWARD_GOLD", 0);
				io.add("REWARD_REP", 0);
				io.add("EXECUTE_PLAYER_CMD", 0);
				io.add("EXECUTE_SERVER_CMD", 0);
				io.write();
				QuestX.logError("Death Trigger file for NPC '" + npcName + "' was not found, it was created automatically.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		io.read();

		if(io.doesKeyExist("REWARD_EXP")){
			awardExp = io.getInt("REWARD_EXP");
		} else {
			throw new MissingDeathTriggerPropertyException(npcName, "REWARD_EXP");
		}
		
		if(io.doesKeyExist("REWARD_GOLD")){
			awardGold = io.getInt("REWARD_GOLD");
		} else {
			throw new MissingDeathTriggerPropertyException(npcName, "REWARD_GOLD");
		}
		
		if(io.doesKeyExist("REWARD_REP")){
			awardRep = io.getInt("REWARD_REP");
		} else {
			throw new MissingDeathTriggerPropertyException(npcName, "REWARD_REP");
		}
		
		if(io.doesKeyExist("EXECUTE_PLAYER_CMD")){
			playerCMD = io.getString("EXECUTE_PLAYER_CMD").split(",");
		} else {
			throw new MissingDeathTriggerPropertyException(npcName, "EXECUTE_PLAYER_CMD");
		}
		
		if(io.doesKeyExist("EXECUTE_SERVER_CMD")){
			serverCMD = io.getString("EXECUTE_SERVER_CMD").split(",");
		} else {
			throw new MissingDeathTriggerPropertyException(npcName, "EXECUTE_SERVER_CMD");
		}
	}
	
	public void execute(Player p){
		if(awardRep != 0){
			ReputationManager.updateReputation(p.getName(), awardRep);
		}
		if(awardGold > 0){
			if (QuestX.economy.hasAccount(p.getName())) {
				QuestX.economy.bankDeposit(p.getName(), awardGold);
			} else {
				// can't award, no account
			}
		}
		if(awardExp >= 1){
			for (int exp = 1; exp <= awardExp; exp++) {
				ExperienceOrb orb = p.getWorld().spawn(p.getLocation().add(0.5, 10, 0.5), ExperienceOrb.class);
				orb.setExperience(1);
			}
		}
		
		if (playerCMD.length > 0 && !playerCMD[0].equalsIgnoreCase("0")) {
			QuestXCMDExecutor.executeAsPlayer(p.getName(), playerCMD);
		}

		if (serverCMD.length > 0 && !playerCMD[0].equalsIgnoreCase("0")) {
			QuestXCMDExecutor.executeAsServer(serverCMD);
		}
	}

}
