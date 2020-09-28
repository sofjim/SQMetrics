package metrics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;


public class ClassObj {
	
	//**FIELDS*********************************************************************
	protected static ArrayList<Class<?>> classes;
	String plainName;
	Field [] fields;
	//MethodObj [] methods;
	Class<?> currentClass;
	Constructor <?> [] constructors;
	Class <?> superClass;
	String [] sourceCode;
	
	//**CONSTRUCTOR********************************************************************

	public ClassObj(Class <?> c) {
		
		plainName = plName(c);
		
		//Method [] meths = MethodObj.purifyMethods(c);
		
		//methods = new MethodObj [meths.length];
		
		//for (int i = 0; i < meths.length; i++) {
			//methods [i] = new MethodObj (meths [i]);
			
		//}
		
		
		
		fields = c.getDeclaredFields();
		
		currentClass = c;
		
		constructors = c.getConstructors();
		
		superClass = c.getSuperclass();
		
		
	}
	
	public static String plName (Class<?> c) {
		
		String name = c.getName();
		int n = name.lastIndexOf('.');
		if (n >= 0) {
			name = c.getName().substring(n + 1);
		}
		return name;
		
	}

	public static void enlistClasses () {
		
		for (ClassFile classFile : ClassFile.classFiles) {
			String qualName = classFile.qualName();
			Path p = classFile.getClassPath();
			String classPath = p.toString();
			Class<?> newClass;
			try {
				newClass = ClassFile.initClass(classPath, qualName);
			classes.add(newClass);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<Class<?>> getChildren (Class<?> c) {
		/**
		 * This method returns an array list that contains the children of a class.
		 */
	
		ArrayList<Class<?>> children = new ArrayList<Class<?>>();
		String a = c.getName();
	
		// check all the classes of the project for superclasses
		for (Class<?> clas : classes) {
			Class<?> sc = clas.getSuperclass();
			
			// if a class has superclass, check if this superclass is the class we measure
			if (sc != null) {
				String b = sc.getName();
				
				// if so, add the child to the array list
				if (a.equals(b)) {
					children.add(clas);
				}
			}
		}
		return children;
	}
	
	public static ArrayList <Field> instanceFields (Class <?> c){
		
		// Get the fields of the class
		Field [] allFields = c.getDeclaredFields();
		
		// get the instance (non static) fields of the class
		ArrayList <Field> fields = new ArrayList <> ();
		for (Field f : allFields) {
			if (!Modifier.isStatic(f.getModifiers())){
				fields.add(f);
			}
		}
		return fields;
	}

	public static ArrayList <Field> staticFields (Class <?> c){
		
		// Get the fields of the class
		Field [] allFields = c.getDeclaredFields();
		
		// get the instance (non static) fields of the class
		ArrayList <Field> fields = new ArrayList <> ();
		for (Field f : allFields) {
			if (Modifier.isStatic(f.getModifiers())){
				fields.add(f);
			}
		}
		return fields;
	}

	public static ArrayList <Method> methodsAccessField (Class<?> c, Field f) {
		/**
		 * This method returns an ArrayList that contains
		 * the methods of the class c, which access the field f.
		 */
		
		// This ArrayList will be returned
		ArrayList <Method> accessMethods= new ArrayList <> ();
		
		// Get the methods and the fields of the class c
		Method [] methods = MethodObj.purifyMethods(c);
				
		for (int i = 0; i < methods.length; i++) {
			
			// get the source code of the method as an array. Each cell is 1 line of code
			String [] source = MethodObj.sourCode (methods [i]);
			for (int line = 0; line < source.length; line++) {
			boolean access = false;	
				// split to words only
				String[] splitedString = source [line].split("[^\\w']+");	
				
				// check if the name of the field exists into the code
				
				for (int a = 0; a < splitedString.length; a++) {
					if (splitedString [a].equals(f.getName())) {
						accessMethods.add (methods [i]);
						access = true;
						break;
					}
				}
				
				// If the field has been found, there is no need to continue checking the rest of the code
				if (access == true) break;
			}			
		}
		return accessMethods;
	}

	
}
