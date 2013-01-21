package com.adamki11s.npcs.tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import com.adamki11s.questx.QuestX;


public class Fireworks {

	final Location root;
	final int radius, sectors;
	final Random r = new Random();

	public Fireworks(Location root,int radius, int sectors) {
		this.root = root;
		this.radius = radius;
		this.sectors = sectors;
	}
	
	public void fireLocatorBeacons(){
		Bukkit.getServer().getScheduler().runTaskAsynchronously(QuestX.p, new Runnable(){ 
			
			public void run(){
				for(int i = 1; i < 20; i++){
					Location launch = new Location(root.getWorld(), root.getX(), 70 + i, root.getZ());
					Firework fw = root.getWorld().spawn(launch, Firework.class);
					FireworkMeta fwm = fw.getFireworkMeta();
					FireworkEffect effect = FireworkEffect.builder().withColor(Color.fromRGB(r.nextInt(255) + 1, r.nextInt(255) + 1, r.nextInt(255) + 1))
							.with(Type.BALL_LARGE).build();
					fwm.addEffects(effect);
					fwm.setPower(0);
					fw.setFireworkMeta(fwm);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
	}
	
	public void circularDisplay(){
		Bukkit.getServer().getScheduler().runTaskAsynchronously(QuestX.p, new Runnable(){ 
			
			public void run(){
				double degrees = (360 / sectors);
				for(int i = 1; i < sectors; i++){
					double xOffset = ((radius) * Math.cos((Math.PI / 180) * (degrees * i)));
					double zOffset = ((radius) * Math.sin((Math.PI / 180) * (degrees * i)));
					Location launch = new Location(root.getWorld(), root.getX() + xOffset, root.getY(), root.getZ() + zOffset);
					Firework fw = root.getWorld().spawn(launch, Firework.class);
					FireworkMeta fwm = fw.getFireworkMeta();
					FireworkEffect effect = FireworkEffect.builder().withColor(Color.fromRGB(r.nextInt(255) + 1, r.nextInt(255) + 1, r.nextInt(255) + 1))
							.with(Type.BALL_LARGE).build();
					fwm.addEffects(effect);
					fwm.setPower(2);
					fw.setFireworkMeta(fwm);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		
	}

}
