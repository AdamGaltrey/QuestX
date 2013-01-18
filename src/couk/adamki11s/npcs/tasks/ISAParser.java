package couk.adamki11s.npcs.tasks;

import org.bukkit.inventory.ItemStack;

public class ISAParser {
	
	public static ItemStack[] parseISA(String isaRaw){
		String[] components = isaRaw.split(",");
		ItemStack[] isa = new ItemStack[components.length]; 
		int count = 0;
		for(String is : components){
			String[] dataValues = is.split(":");
			int id, quantity;
			byte data;
			try{
				id = Integer.parseInt(dataValues[0]);
				quantity = Integer.parseInt(dataValues[2]);
				data = (byte) Integer.parseInt(dataValues[1]);
			} catch (NumberFormatException nfe){
				throw new NumberFormatException();
			}
			ItemStack isAdd = new ItemStack(id, quantity);
			isAdd.getData().setData(data);
			isa[count] = isAdd;
			count++;
		}
		return isa;
	}

}
