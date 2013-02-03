package com.adamki11s.npcs.tasks;

import org.bukkit.inventory.ItemStack;

import com.adamki11s.exceptions.InvalidISAException;
import com.adamki11s.questx.QuestX;

public class ISAParser {

	public static synchronized ItemStack[] parseISA(String isaRaw, String cause, boolean quest) throws InvalidISAException {
		QuestX.logMSG("Beinning ISA parse = " + isaRaw);
		String[] components = isaRaw.split("#");
		ItemStack[] isa = new ItemStack[components.length];
		int count = 0;
		QuestX.logMSG("Starting split loop");
		for (String is : components) {
			String[] dataValues = is.split(",");
			int id, quantity;
			byte data;
			try {
				try {
					id = Integer.parseInt(dataValues[0]);
					quantity = Integer.parseInt(dataValues[2]);
					data = (byte) Integer.parseInt(dataValues[1]);
				} catch (NullPointerException npe) {
					throw new InvalidISAException(is,
							"ID, quantity or data was null, check your array is configured correctly in the format <id>:<data>:<quantity>,<id2>:<data2>:<quantity2>, etc...", cause, quest);
				}
			} catch (NumberFormatException nfe) {
				QuestX.logMSG("NFE thrown!!");
				throw new InvalidISAException(is, "ID, quantity or data was not an integer value.", cause, quest);
			}
			ItemStack isAdd = new ItemStack(id, quantity);
			isAdd.getData().setData(data);
			isa[count] = isAdd;
			count++;
		}
		QuestX.logMSG("Split loop ended, returning");
		return isa;
	}

}
