package fr.glowstoner.fireapi.crypto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EncryptedList extends EncryptFunction<List<?>, String>{

	public EncryptedList(String key) {
		super(key, EncryptValueType.ENCRYPT_LIST);
	}

	@Override
	public String encrypt(List<?> e) throws Exception {
		if(e.isEmpty()) {
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		
		for(Object el : e) {
			builder.append("*"+el.getClass().getName()+"@"+new FireEncrypt(super.key).encrypt(el.toString())+"!");
		}
		
		return new FireEncrypt(super.key).encrypt(builder.toString());
	}

	@Override
	public List<?> decrypt(String d) throws Exception {
		if(d.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<Object> list = new ArrayList<>();  
		
		for(String line : new FireEncrypt(super.key).decrypt(d).split("!")) {
			
			String className = null, value = null;
			
			for(String part : line.split("@")) {
				if(part.startsWith("*")) {
					className = part.substring(1);
				}else {
					value = part;
				}
			}
			
			FireEncrypt crypt = new FireEncrypt(super.key);
			
			if(className.equals("java.lang.Character")) {
				list.add(crypt.decrypt(value).charAt(0));
				continue;
			}else if(className.equals("java.lang.String")) {
				list.add(crypt.decrypt(value));
				continue;
			}
			
			Method m = Class.forName(className).getMethod("valueOf", String.class);
			
			list.add(m.invoke(null, crypt.decrypt(value)));
		}
		
		return list;
	}
}