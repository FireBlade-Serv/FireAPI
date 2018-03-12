package fr.glowstoner.fireapi.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationUtil {
	
	public static Direction getFacingDirection(Player p){
		float yaw = p.getLocation().getYaw();
		if (yaw < 0) {
			yaw += 360;
		}
		 
		if (yaw >= 315 || yaw < 45) {
			return Direction.SOUTH;
		} else if (yaw < 135) {
			return Direction.WEST;
		} else if (yaw < 225) {
			return Direction.NORTH;
		} else if (yaw < 315) {
			return Direction.EAST;
		}
		
		return Direction.NORTH;
	}

	public static Location getLocationInOppositeDirection(Player p, double distance) {
		Direction opp = Direction.valueOf(getFacingDirection(p).getOpposite());
		
		if(opp.equals(Direction.NORTH) || opp.equals(Direction.NORTH_EAST) ||
				opp.equals(Direction.NORTH_NORTH_EAST) || opp.equals(Direction.NORTH_NORTH_WEST) ||
				opp.equals(Direction.NORTH_WEST)) {
			
			return p.getLocation().subtract(0, 0, distance);
		}else if(opp.equals(Direction.SOUTH) || opp.equals(Direction.SOUTH_EAST) ||
				opp.equals(Direction.SOUTH_SOUTH_EAST) || opp.equals(Direction.SOUTH_SOUTH_WEST) ||
				opp.equals(Direction.SOUTH_WEST)) {
			
			return p.getLocation().add(0.0d, 0.0d, distance);
		}else if(opp.equals(Direction.WEST) || opp.equals(Direction.WEST_NORTH_WEST) ||
				opp.equals(Direction.WEST_SOUTH_WEST)) {
			
			return p.getLocation().subtract(distance, 0.0d, 0.0d);
		}if(opp.equals(Direction.EAST) || opp.equals(Direction.EAST_NORTH_EAST) ||
				opp.equals(Direction.EAST_SOUTH_EAST)) {
			
			return p.getLocation().add(distance, 0.0d, 0.0d);
		}else {
			return null;
		}
	}
	
	public static Location getLocationInLookingDirection(Player p, double distance) {
		Direction opp = getFacingDirection(p);
		
		if(opp.equals(Direction.NORTH) || opp.equals(Direction.NORTH_EAST) ||
				opp.equals(Direction.NORTH_NORTH_EAST) || opp.equals(Direction.NORTH_NORTH_WEST) ||
				opp.equals(Direction.NORTH_WEST)) {
			
			return p.getLocation().subtract(0, 0, distance);
		}else if(opp.equals(Direction.SOUTH) || opp.equals(Direction.SOUTH_EAST) ||
				opp.equals(Direction.SOUTH_SOUTH_EAST) || opp.equals(Direction.SOUTH_SOUTH_WEST) ||
				opp.equals(Direction.SOUTH_WEST)) {
			
			return p.getLocation().add(0.0d, 0.0d, distance);
		}else if(opp.equals(Direction.WEST) || opp.equals(Direction.WEST_NORTH_WEST) ||
				opp.equals(Direction.WEST_SOUTH_WEST)) {
			
			return p.getLocation().add(distance, 0.0d, 0.0d);
		}if(opp.equals(Direction.EAST) || opp.equals(Direction.EAST_NORTH_EAST) ||
				opp.equals(Direction.EAST_SOUTH_EAST)) {
			
			return p.getLocation().subtract(distance, 0.0d, 0.0d);
		}else {
			return null;
		}
	}
	
	public static double getDistance(Location loc1, Location loc2) {
		//distance = sqrt[ (x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2 ]
		
		return Math.sqrt(Math.pow((loc1.getX() - loc2.getX()), 2) +
				Math.pow((loc1.getY() - loc2.getY()), 2) + Math.pow((loc1.getZ() - loc2.getZ()), 2));
	}
}
