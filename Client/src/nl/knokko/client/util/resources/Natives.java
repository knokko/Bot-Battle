package nl.knokko.client.util.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Natives {
	
	public static void prepare(File gameFolder){
		File destFolder = new File(gameFolder.getPath() + File.separator + "natives");
		if(!destFolder.exists()){
			System.out.println("natives folder does not exist; creating a new one");
			destFolder.mkdirs();
			
			try {
				URL url = Natives.class.getClassLoader().getResource("natives");
				URLConnection uc = url.openConnection();
				if(uc instanceof JarURLConnection){//not in development
					JarURLConnection jc = (JarURLConnection) uc;
					JarFile jar = jc.getJarFile();
					Enumeration<JarEntry> entries = jar.entries();
					while(entries.hasMoreElements()){
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if(name.startsWith("natives")){
							File f = new File(gameFolder + File.separator + entry.getName());
							if(entry.isDirectory()){
								f.mkdirs();
							}
							else {
								copy(jar.getInputStream(entry), new FileOutputStream(f));
							}
						}
					}
				}
				else {//in development
					System.out.println("Not a jar connection (" + uc + ")");
					copyFile(new File("client/natives"), gameFolder);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("natives folder already exists");
		}
		System.setProperty("org.lwjgl.librarypath", destFolder.getAbsolutePath());
	}
	
	private static void copyFile(File source, File dest) throws IOException {
		File[] children = source.listFiles();
		if(children == null){//source is a file
			File newFile = new File(dest + File.separator + source.getName());
			copy(new FileInputStream(source), new FileOutputStream(newFile));
		}
		else {//source is a directory
			File newFolder = new File(dest + File.separator + source.getName());
			newFolder.mkdirs();
			for(File child : children){
				copyFile(child, newFolder);
			}
		}
	}
	
	private static void copy(InputStream input, OutputStream output) throws IOException{
		byte[] buffer = new byte[100000];
		int readBytes = input.read(buffer);
		while(readBytes != -1){
			output.write(buffer, 0, readBytes);
			readBytes = input.read(buffer);
		}
		input.close();
		output.close();
	}
}