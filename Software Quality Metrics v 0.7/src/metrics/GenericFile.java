package metrics;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GenericFile extends java.io.File {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2142925643337885997L;
	
	protected String plainName;		

	//Constructor
	public GenericFile (String pathname, String plName) {
		super(pathname);
		this.plainName = plName;		
	}
	
	public void setPlainName(String value) {
	   this.plainName = value;
	}
	
	public String getPlainName() {
		
		String thisName = this.getName();
		return plainName = removeExtension (thisName);
	}
	
	public static void enlist (String path) {
		
		/**
		 * This method creates 3 Array Lists containing all the
		 * *.java files, the *.class files 
		 * and the classes of the project (as objects)
		 */
		
		JavaFile.javaFiles = new ArrayList<JavaFile>();
		ClassFile.classFiles = new ArrayList<ClassFile>();
		ClassObj.classes = new ArrayList<Class<?>>();
		
		enlistFiles (path);
		
		while (ClassFile.purifyClassFiles() == true) {
			/*
			 * If returns "true", it means that at least one clone class has been found.
			 * The method will be executed multiple times, till it returns "false",
			 * which means that no clone class found.
			 */
			
			ClassFile.purifyClassFiles();
		}
		
		ClassObj.enlistClasses();
		
//		System.out.println ("Java files: " + JavaFile.javaFiles);
//		System.out.println ("Class files: " + ClassFile.classFiles);
//		System.out.println ("Classes: " + ClassObj.classes);
		
	}
	
	public static void enlistFiles (String path) {
		// This method gets the list of all the files in a directory
	
		File directory = new File (path);
		File [] filesList = directory.listFiles ();
		
		
		if (filesList != null) {
			for (File file : filesList) {
				String name = file.getName();
				String type = getFileExtension(name);
				
				// check if the file is a "java" file and add it to the "javaFiles" array list
				if (file.isFile() && type.equals("java")) {
					String filePath = file.getPath();
					String plainName = removeExtension(file.getName());
					Path javaPath = Paths.get(file.getParent());
					JavaFile jf = new JavaFile(filePath, plainName, javaPath);
					JavaFile.javaFiles.add(jf);
				}
				
				// check if the file is a "class" file and add it to the "classFiles" array list
				else if (file.isFile() && type.equals("class")) {
					String filePath = file.getPath();
					String plainName = removeExtension(file.getName());
					Path classPath = Paths.get(file.getParent());
					ClassFile cf = new ClassFile(filePath, plainName, classPath);
					ClassFile.classFiles.add(cf);
				}
				
				// if the file is directory call this method recursively 
				else if (file.isDirectory()) {
					String fName = file.getAbsolutePath();
					enlistFiles(fName);
				}
			}

		}
		
	}

	public static String getFileExtension (String fileName) {
		// This method gets the extension of a file
		
		String extension = "";

		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
		    extension = fileName.substring(i+1);
		}
		
		return extension;
		
	}
	
	public static String removeExtension(String name) {
		String plainName = name.replaceFirst("[.][^.]+$", "");
		return plainName;
	}

}
