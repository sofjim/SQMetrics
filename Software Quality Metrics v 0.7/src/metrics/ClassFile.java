package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;



public class ClassFile extends GenericFile {
	
	private static final long serialVersionUID = 3340790598345991955L;
	protected static ArrayList<ClassFile> classFiles;
	private Path classPath;

	public ClassFile(String pathname, String plainName, Path cPath) {
		super(pathname, plainName);
		this.setClassPath(cPath);
	}
	
	public void setClassPath(Path value) {
		this.classPath = value;
	}

	public Path getClassPath() {
	   return this.classPath;
	}
	
	public String qualName() {
		/** 
		 * This method gets the fully qualified name for the current class
		 * e.g: packagename.classname (without extension)
		 */
		
		String plName = this.plainName;
		String qualName = null;
		String pack = null;
		
		ArrayList<JavaFile> jFiles = JavaFile.getJavaFiles();
		
		for (JavaFile jn : jFiles) {
			if (jn.plainName.equals(plName)) {
				
				pack = jn.getPackage();
				break;
			}
		}
		if (pack == null) {
			qualName = plName; // If there is no package, the qualName name is the plain name of the class
		}
		else {
			// If package exists, the qualName name is the package's name, followed by a "." and the plain name of the class
			qualName = pack + "." + plName; 
			
			// Count how many dots exist, into the package name
			int d = JavaFile.getIndexesOfChar(pack, '.').size();
			for (int i = 0; i <= d; i++) {
				this.classPath = classPath.getParent();
			}
			
		}
		return qualName;
	}
	
	public static ArrayList<ClassFile> getClassFiles() {
		return classFiles;
	}

	public static String qualName(String plName) {
		/** 
		 * This method gets the fully qualified name for the name "plName"
		 * e.g: packagename.classname (without extension)
		 */
		
		String qualName;
		String pack = null;
		
		ArrayList<JavaFile> jFiles = JavaFile.getJavaFiles();
		for (JavaFile jn : jFiles) {
			if (jn.plainName.equals(plName)){
				pack = jn.getPackage();
			}
		}
		
		if (pack == null ) {
			qualName = plName; // If there is no package, the qualName name is the plain name of the class
		}
		else {
			// If package exists, the qualName name is the package's name, followed by a "." and the plain name of the class
			qualName = pack + "." + plName; 
		}
		return qualName;
	}
	
	static boolean purifyClassFiles() {
		/** Sometimes clone classes are been created during their equivalent class's execution  
		 * and should not be calculated, so they must be removed from the list.
		 */
		
		String className = null;
		ClassFile classFile = null;
		
		for (ClassFile classF : classFiles) {
			boolean exists = false;
			classFile = classF;
			className = GenericFile.removeExtension(classFile.getName());
			
			for (JavaFile javaFile : JavaFile.javaFiles) {
				String javaName = GenericFile.removeExtension(javaFile.getName());
				if (className.equals(javaName)) {
					exists = true;
				}
			}
			if (exists == false) { // "false" means that a clone class has been found
				classFiles.remove(classFile);
				return true; // if a clone class has been found, return true
			}				
		}
		return false;
	}

	@SuppressWarnings("resource")
	public static Class<?> initClass (String classPath, String qualName) throws ClassNotFoundException, MalformedURLException {
		/**
		 *  This method returns the Class object of the given name, which exists in the given path
		 */
		
		if (qualName == null) {
			return null;
		}
		else {
			File f = new File(classPath);
			URL[] cp = {f.toURI().toURL()};
			URLClassLoader urlcl = new URLClassLoader(cp);
			Class<?> clas = urlcl.loadClass(qualName);
			return clas;
		}
	}
	
		public static String javaFilePath (Class<?> c) {
		// Get the corresponding javaFileName
		String javaName = null;
		for (JavaFile j : JavaFile.javaFiles) {
			if (c.getSimpleName().equals(j.plainName)) {
				javaName = j.getPath();
			}
		}
		
		return javaName;
	}
		
		public static String [] classCode (Class<?> c) {
			/**
			 * This method returns an array containing the source code of the class.
			 * Each cell is 1 line of code. 
			 */
		
			String javaName = ClassFile.javaFilePath(c);
			int totalLines = 0; // Metrics.linesOfCode(c);
			String [] cCode = new String [totalLines];
			
			// scan the file that contains the source code
					Scanner sc;
					try {
						sc = new Scanner (new File(javaName), "UTF-8");
						String input;
						int line = 0;
						int index = 0;
						while (sc.hasNextLine() && line <= totalLines) {	
							input = sc.nextLine();
							
								// register the code lines into the array
								cCode [index] = input;
								index++;
							line++;
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					
					for (int i = 0; i < cCode.length; i++) {
						
						// check if it is comment
						int a = JavaFile.checkIfComment (cCode [i]);
						
						// cut the comment part (if exists) and keep the code
						if (a == 0 || a == 1) {
							int n = cCode [i].length();
							if (cCode [i].contains("//")) {
								n = cCode [i].indexOf("//");
							}
							else if (cCode [i].contains("/*")) {
								n = cCode [i].indexOf("/*");
							}
							cCode [i] = cCode [i].substring(0, n);
						}
						
						// if it is comment, it's not code. Make it a blank line.
						else if (a == -1) {
							cCode [i] = " ";
						}
						// if there is any string into quotes, it's not code. Cut them.
						cCode [i] = JavaFile.clearInsideStrings(cCode [i]);
					}
			return cCode;
		}

		public static void setClassFiles(ArrayList<ClassFile> classFiles) {
			ClassFile.classFiles = classFiles;
		}
}
