package edu.carleton.comp4905.carcassonne.client;

public class PositionData {
	protected String name;
	protected int id;
	public final static int INVALID_ID = 0;
	
	public PositionData(final String name) {
		this(name, INVALID_ID);
	}
	
	public PositionData(final String name, final int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "PositionData (name=" + name + ", id=" + id + ")";
	}
}
