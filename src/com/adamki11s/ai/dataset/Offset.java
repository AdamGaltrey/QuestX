package com.adamki11s.ai.dataset;

public class Offset {
	
	private final short x, y, z;

	public Offset(short x, short y, short z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public short getX() {
		return x;
	}

	public short getY() {
		return y;
	}

	public short getZ() {
		return z;
	}
	
	public boolean equals(Offset o){
		return (x == o.getX() && y == o.getY() && z == o.getZ());
	}
	

}
