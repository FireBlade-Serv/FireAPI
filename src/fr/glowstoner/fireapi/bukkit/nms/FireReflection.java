package fr.glowstoner.fireapi.bukkit.nms;

import java.lang.reflect.Field;

public class FireReflection {
	
	public static final String VERSION = "v1_8_R3";
	public static final String PATH = "net.minecraft.server.v1_8_R3";
	
	public static Class<?> getClass(String cname) {
		String path = PATH+"."+cname;
		
		try {
			return Class.forName(path);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Object getFieldValueByName(Object instance, String fname) {
		try {
			Field f = instance.getClass().getDeclaredField(fname);
			f.setAccessible(true);
			
			return f.get(instance);
		} catch (NoSuchFieldException | SecurityException |
				IllegalArgumentException | IllegalAccessException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Field getField(Class<?> clazz, String fname) {
		Field f = null;
		
		try {
			f = clazz.getDeclaredField(fname);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		f.setAccessible(true);
		
		return f;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Field f, Object instance) {
		try {
			return (T) f.get(instance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
