package com.adamki11s.npcs.population;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;
import com.adamki11s.questx.sql.SQLTables;
import com.adamki11s.sync.sql.SyncSQL;

public class PopulationDensityThread implements Runnable {

	final SyncSQL sql;
	String[] worlds;
	
	int gdCacheUpdate = 0;

	HashMap<String, PreparedStatement[]> preparedStatements = new HashMap<String, PreparedStatement[]>();

	public PopulationDensityThread() {
		this.sql = new SyncSQL(FileLocator.getPopDensityDatabase());
		SQLTables.initiateSQLite(sql);
		this.worlds = WorldConfigData.getWorlds();
		this.loadPreparedStatements();
		GlobalDensityCache.updateGlobalDensity(this.sql, worlds);
	}

	void loadPreparedStatements() {
		// this.sql.getConnection().setAutoCommit(false);
		for (String worldName : this.worlds) {
			// if exists (select * from tablename where zipcode = value)
			// select 'already exists'
			// else
			// select 'no record'
			/*String prepUpdateDensity = "IF EXISTS(SELECT x,z FROM " + worldName + " WHERE x=? AND z=?) THEN UPDATE " + worldName
					+ " SET density=density+? WHERE x=? AND z=?; ELSE" + " INSERT INTO " + worldName + " (x,z,density) VALUES (?,?,?);";*/
			QuestX.logMSG("Preparing statements for world '" + worldName + "'");
			String selectXZ = "SELECT x,z FROM " + worldName + " WHERE x=? AND z=?";
			String updateDensity = "UPDATE " + worldName + " SET density=density+? WHERE x=? AND z=?";

			try {
				PreparedStatement prepXZ = this.sql.getConnection().prepareStatement(selectXZ),
				prepDensity = this.sql.getConnection().prepareStatement(updateDensity);
				
				this.preparedStatements.put(worldName, new PreparedStatement[]{prepXZ, prepDensity});
				QuestX.logMSG("Statement prepared for world '" + worldName + "'.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// String updateDensity = "UPDATE " + worldName +
			// " SET x=x+1, z=z+1 WHERE x=?, z=?";
			// String checkChunk = "SELECT (x,z) FROM pop_density_" + worldName
			// + " WHERE x=?, z=?";
		}
	}

	boolean canSpawnInWorld(String w) {
		for (String wn : this.worlds) {
			if (w.equalsIgnoreCase(wn)) {
				return true;
			}
		}
		return false;
	}

	public void terminateSQL(){
		this.sql.closeConnection();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if(this.gdCacheUpdate < 25){
			this.gdCacheUpdate++;
		} else {
			this.gdCacheUpdate = 0;
			GlobalDensityCache.updateGlobalDensity(this.sql, worlds);
		}
		
		QuestX.logMSG("Updating , time elapsed = 5 minutes");
		if (Bukkit.getServer().getOnlinePlayers().length < 1) {
			return;
		}
		for (World w : Bukkit.getServer().getWorlds()) {
			if (!this.canSpawnInWorld(w.getName())) {
				continue;
			} else {
				HashSet<ChunkDensity> density = new HashSet<ChunkDensity>();
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (!(p.getWorld().getName().equalsIgnoreCase(w.getName()))) {
						continue;
					} else {
						Chunk c = p.getLocation().getChunk();
						boolean register = true;
						for (ChunkDensity cd : density) {
							if (cd.isDuplicate(c.getX(), c.getZ())) {
								register = false;
								cd.increment();
								break;
							}
						}
						if (!register) {
							continue;
						} else {
							ChunkDensity chunkDensity = new ChunkDensity(c.getX(), c.getZ());
							density.add(chunkDensity);
						}

					}
				}
				this.updateSQLTable(w.getName(), density);
			}
		}
	}

	void updateSQLTable(String worldName, HashSet<ChunkDensity> density) {

		PreparedStatement[] preps = this.preparedStatements.get(worldName);
		
		/*String selectXZ = "SELECT x,z FROM " + worldName + " WHERE x=? AND z=?";
		String updateDensity = "UPDATE " + worldName + " SET density=density+? WHERE x=? AND z=?";
		String insertChunk = "INSERT INTO " + worldName + " (x,z,density) VALUES (x=?,z=?,density=?)";*/
		
		for(ChunkDensity cd : density){
			try {
				QuestX.logMSG("Updating data for chunk (" + cd.getX() + ", " + cd.getZ() + ") World - " + worldName);
				preps[0].setInt(1, cd.getX());
				preps[0].setInt(2, cd.getZ());
				ResultSet xzCheck = preps[0].executeQuery();
				if(xzCheck.next()){
					QuestX.logMSG("Chunk exists, updating...");
					preps[1].setInt(1, cd.getIncrement());
					preps[1].setInt(2, cd.getX());
					preps[1].setInt(3, cd.getZ());
					preps[1].executeUpdate();
				} else {
					QuestX.logMSG("Chunk doesn't exist, creating...");
					String insertChunk = "INSERT INTO " + worldName + " (x,z,density) VALUES (" + cd.getX() + "," + cd.getZ() + "," + cd.getIncrement() + ")";
					this.sql.standardQuery(insertChunk);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// updateSales.setInt(1, e.getValue().intValue());
		// updateSales.setString(2, e.getKey());
		// updateSales.executeUpdate();
		// updateTotal.setInt(1, e.getValue().intValue());
		// updateTotal.setString(2, e.getKey());
		// updateTotal.executeUpdate();
	}

}
