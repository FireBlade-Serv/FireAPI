package fr.glowstoner.fireapi.utils;

public enum Direction {

	NORTH("SOUTH"), NORTH_EAST("SOUTH_WEST"), NORTH_WEST("SOUTH_EAST"),
	NORTH_NORTH_EAST("SOUTH_SOUTH_WEST"), NORTH_NORTH_WEST("SOUTH_SOUTH_EAST"), 
	SOUTH("NORTH"), SOUTH_EAST("NORTH_WEST"), SOUTH_WEST("NORTH_EAST"),
	SOUTH_SOUTH_EAST("NORTH_NORTH_WEST"), SOUTH_SOUTH_WEST("NORTH_NORTH_EAST"),
	EAST("WEST"), EAST_NORTH_EAST("WEST_SOUTH_WEST"), EAST_SOUTH_EAST("WEST_NORTH_WEST"),
	WEST("EAST"), WEST_NORTH_WEST("EAST_SOUTH_EAST"), WEST_SOUTH_WEST("EAST_NORTH_EAST");
	
	private String opposite;
	
	private Direction(String opposite) {
		this.opposite = opposite;
	}
	
	public String getOpposite() {
		return this.opposite;
	}
}
