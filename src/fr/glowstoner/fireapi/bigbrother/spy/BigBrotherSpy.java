package fr.glowstoner.fireapi.bigbrother.spy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BigBrotherSpy {
	
	private File folder;
	
	public BigBrotherSpy() {
		
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
				
				File file = new File(path.toString() + "/spy/");
				
				System.out.println("[BigBrother] Création du dossier spy \""+file.getPath()+
						"\" en cours ... Résulat = "+file.mkdirs());
				
				this.folder = file;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createNewDataFile(String name, BigBrotherSpyHistory history) {
		if(!this.ifDataFileExists(name)) {
			File f = new File(this.folder.getPath()+"/"+name+".firespy");
			
			try {
				System.out.println("[BigBrother] Création d'un fichier pour "+name+" : "+
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
		Path path = null;
		
		try {
			path = Paths.get(ClassLoader.getSystemResource("").toURI());
			
			for(File files : path.toFile().listFiles()) {
				if(files.isDirectory() && files.getName().equals("spy")) {
					if(new File(files.getPath()+"/"+name+".firespy").exists()) {
						return true;
					}
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void updateDataFile(String name, BigBrotherSpyHistory history) {
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
	
	public BigBrotherSpyHistory getHistory(String name) {
		if(this.ifDataFileExists(name)) {
			File f = new File(this.folder.getPath()+"/"+name+".firespy");
			
			try {
				FileInputStream fi = new FileInputStream(f);
				ObjectInputStream oi = new ObjectInputStream(fi);
				
				BigBrotherSpyHistory h = (BigBrotherSpyHistory) oi.readObject();
				
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
