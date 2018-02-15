package fr.glowstoner.fireapi.gediminas.spy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GediminasSpy {
	
	private File folder;
	
	public GediminasSpy() {
		
	}
	
	public void initFolder() {
		if(!this.hasFolder()) {
			this.createFolder();
		}
	}
	
	public boolean hasFolder() {
		Path path = null;
		
		try {
			path = Paths.get(ClassLoader.getSystemResource("").toURI());
			
			for(File files : path.toFile().listFiles()) {
				if(files.getName().equals("spy")){
					if(files.isDirectory()) {
						this.folder = files;
						
						return true;
					}
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public void createFolder() {
		if(!hasFolder()) {
			Path path = null;
			
			try {
				path = Paths.get(ClassLoader.getSystemResource("").toURI());
				
				File file = new File(path.toString() + "\\spy\\");
				file.mkdirs();
				
				this.folder = file;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createNewDataFile(String name, GediminasSpyHistory history) {
		if(!this.ifDataFileExists(name)) {
			File f = new File(this.folder.getPath()+"/"+name+".firespy");
			
			try {
				System.out.println("[Gediminas] Création d'un fichier pour "+name+" : "+
						this.folder.getPath()+"/"+name+".firespy");
				
				f.createNewFile();
				
				FileOutputStream fo = new FileOutputStream(f);
				ObjectOutputStream oo = new ObjectOutputStream(fo);
				
				oo.writeObject(history);
				
				oo.close();
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean ifDataFileExists(String name) {
		return new File(this.folder.getPath()+"/"+name+".firespy").exists();
	}
	
	public void updateDataFile(String name, GediminasSpyHistory history) {
		if(this.ifDataFileExists(name)) {
			File f = new File(this.folder.getPath()+"/"+name+".firespy");
			
			try {
				FileOutputStream fo = new FileOutputStream(f);
				ObjectOutputStream oo = new ObjectOutputStream(fo);
				
				oo.writeObject(history);
				
				oo.close();
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public GediminasSpyHistory getHistory(String name) {
		if(this.ifDataFileExists(name)) {
			File f = new File(this.folder.getPath()+"/"+name+".firespy");
			
			try {
				FileInputStream fi = new FileInputStream(f);
				ObjectInputStream oi = new ObjectInputStream(fi);
				
				GediminasSpyHistory h = (GediminasSpyHistory) oi.readObject();
				
				oi.close();
				fi.close();
				
				return h;
			}catch(IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		
		return null;
	}
	
	public File getFolder() {
		return this.folder;
	}
}
