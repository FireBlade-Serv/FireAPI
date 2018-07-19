package fr.glowstoner.fireapi.utils;

public class PlatformUtil {
	
	public static PlatformType getPlatform() {
		String os = System.getProperty("os.name").toLowerCase();
		
		if(os.equals("linux")) {
			return PlatformType.LINUX;
		}else if(os.startsWith("windows")) {
			return PlatformType.WINDOWS;
		}else if(os.contains("mac")) {
			return PlatformType.MACOS;
		}else {
			return PlatformType.OTHER;
		}
	}
	
}
