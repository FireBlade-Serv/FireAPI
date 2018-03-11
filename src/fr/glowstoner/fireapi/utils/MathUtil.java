package fr.glowstoner.fireapi.utils;

public class MathUtil {
	
	public static double getRandomWithMin(double max, double min) {
		if(min >= max) {
			throw new IllegalArgumentException("La valeur maximum doit être plus grande que la valeur mininum !");	
		}		
		
		double n = 0.0d;
		
		while(n < min) {
			n = Math.random() * max;
		}
		
		return n;
	}

}
