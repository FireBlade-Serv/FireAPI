package fr.glowstoner.fireapi.gediminas.spy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GediminasSpy {
	
	private File folder;
	
	public GediminasSpy() {
		
	}
	
	public void initFolder() {
		File f = new File("/spy/");
		
		if(!f.exists()) {
			f.mkdirs();
		}
		
		this.folder = f;
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
		return new File(this.folder.getPath()+"/"+name+".dat").exists();
	}
	
	public void updateDataFile(String name, GediminasSpyHistory history) {
		if(this.ifDataFileExists(name)) {
			File f = new File(this.folder.getPath()+"/"+name+".dat");
			
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
			File f = new File(this.folder.getPath()+"/"+name+".dat");
			
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
