package couk.adamki11s.extras.inventory;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExtrasInventory {
	
	private HashMap<Player, ItemStack[]> playerInventory = new HashMap<Player, ItemStack[]>();
 
	
	public void addToInventory(Player p, Material m, int quantity) {
		p.getInventory().addItem(new ItemStack(m.getId(), quantity));
	}

	
	public void addToInventory(Player p, int id, int quantity) {
		p.getInventory().addItem(new ItemStack(id, quantity));
	}

	
	public void removeFromInventory(Player p, Material m, int quantity) {
		int amount = getAmountOf(p, m.getId());
		p.getInventory().remove(m.getId());
		addToInventory(p, m.getId(), amount - quantity);
	}

	
	public void removeFromInventory(Player p, int id, int quantity) {
		int amount = getAmountOf(p, id);
		p.getInventory().remove(id);
		addToInventory(p, id, amount - quantity);
	}

	
	public int getEmptySlots(Player p) {
		ItemStack[] invent = p.getInventory().getContents();
		int freeSlots = 0;
		for(ItemStack i : invent){
			if(i == null){
				freeSlots++;
			}
		}
		return freeSlots;
	}

	
	public boolean doesInventoryContain(Player p, Material m) {
		ItemStack[] invent = p.getInventory().getContents();
		for(ItemStack i : invent){
			if(i != null){
				if(i.getType() == m){
					return true;
				}
			}
		}
		return false;
	}

	
	public boolean doesInventoryContain(Player p, int id) {
		ItemStack[] invent = p.getInventory().getContents();
		for(ItemStack i : invent){
			if(i != null){
				if(i.getTypeId() == id){
					return true;
				}
			}
		}
		return false;
	}

	
	public int getStackCount(Player p, Material m) {
		ItemStack[] invent = p.getInventory().getContents();
		int stackCount = 0;
		for(ItemStack i : invent){
			if(i != null){
				if(i.getType() == m){
					stackCount++;
				}
			}
		}
		return stackCount;
	}

	
	public int getStackCount(Player p, int id) {
		ItemStack[] invent = p.getInventory().getContents();
		int stackCount = 0;
		for(ItemStack i : invent){
			if(i != null){
				if(i.getTypeId() == id){
					stackCount++;
				}
			}
		}
		return stackCount;
	}

	
	public int getAmountOf(Player p, Material m) {
		ItemStack[] invent = p.getInventory().getContents();
		int amount = 0;
		for(ItemStack i : invent){
			if(i != null){
				if(i.getType() == m){
					amount += i.getAmount();
				}
			}
		}
		return amount;
	}

	
	public int getAmountOf(Player p, int id) {
		ItemStack[] invent = p.getInventory().getContents();
		int amount = 0;
		for(ItemStack i : invent){
			if(i != null){
				if(i.getTypeId() == id){
					amount += i.getAmount();
				}
			}
		}
		return amount;
	}
	
	
	public void sortInventory(Player p) {
		ItemStack[] invent = p.getInventory().getContents();
		for(ItemStack i : invent){
			if(i != null){
				removeFromInventory(p, i.getType(), 0);
			}
		} 
	}

	
	public void removeAllFromInventory(Player p, int id) {
		if(doesInventoryContain(p, id)){
			p.getInventory().remove(id);
		}
	}

	
	public void removeAllFromInventory(Player p, Material m) {
		if(doesInventoryContain(p, m)){
			p.getInventory().remove(m);
		}	
	}

	
	public void wipeInventory(Player p) {
		// TODO Auto-generated method stub
		for(ItemStack i : p.getInventory().getContents()){
			if(i != null){
				p.getInventory().remove(i.getTypeId());
			}
		}
	}

	
	public boolean storeInventory(Player p) {
		if(p != null){
			playerInventory.put(p, p.getInventory().getContents());
			return true;
		} else {
			return false;
		}
	}

	
	public ItemStack[] retrieveInventory(Player p) {
		if(playerInventory.containsKey(p)){
			return playerInventory.get(p);
		} else {
			return null;
		}
	}

	
	public void updateInventory(Player p) {
		((org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer)p).getHandle().syncInventory();
	}

}
